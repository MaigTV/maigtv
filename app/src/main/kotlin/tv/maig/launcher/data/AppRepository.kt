package tv.maig.launcher.data

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Representation of an installed TV or launcher application.
 *
 * @property packageName Unique application package identifier.
 * @property activityName Fully qualified launcher activity name.
 * @property label User-facing application title.
 * @property banner TV banner graphic, if provided by the application.
 * @property icon Application icon graphic used as fallback when no TV banner exists.
 * @property isLeanback Whether the app was discovered via [Intent.CATEGORY_LEANBACK_LAUNCHER].
 */
data class TvAppInfo(
  val packageName: String,
  val activityName: String,
  val label: String,
  val banner: Drawable? = null,
  val icon: Drawable? = null,
  val isLeanback: Boolean = true,
)

/**
 * Data repository interface providing discovery, lifecycle updates, and execution for installed applications.
 */
interface AppRepository {
  /**
   * Reactive stream emitting the current list of launchable applications,
   * automatically updated when applications are installed, uninstalled, or modified.
   */
  val apps: Flow<List<TvAppInfo>>

  /**
   * Forces a refresh scan of installed applications across the system.
   */
  suspend fun refreshApps()

  /**
   * Launches the specified application, prioritizing native Leanback TV entry points
   * before falling back to standard launcher intents.
   *
   * @return [Result.success] if the launch intent was successfully dispatched, or [Result.failure] with details.
   */
  fun launchApp(packageName: String): Result<Unit>
}

/**
 * Production implementation of [AppRepository] backed by [PackageManager] and [LauncherApps.Callback].
 */
class DefaultAppRepository(
  private val context: Context,
  private val packageManager: PackageManager = context.packageManager,
  private val launcherApps: LauncherApps? = runCatching {
    context.getSystemService(LauncherApps::class.java)
  }.getOrNull(),
  ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AppRepository {

  private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

  override val apps: Flow<List<TvAppInfo>> = callbackFlow {
    fun sendCurrentApps() {
      trySend(queryInstalledApps())
    }

    // Initial emission
    sendCurrentApps()

    val callback = object : LauncherApps.Callback() {
      override fun onPackageAdded(packageName: String, user: UserHandle) {
        sendCurrentApps()
      }

      override fun onPackageRemoved(packageName: String, user: UserHandle) {
        sendCurrentApps()
      }

      override fun onPackageChanged(packageName: String, user: UserHandle) {
        sendCurrentApps()
      }

      override fun onPackagesAvailable(
        packageNames: Array<out String>,
        user: UserHandle,
        replacing: Boolean
      ) {
        sendCurrentApps()
      }

      override fun onPackagesUnavailable(
        packageNames: Array<out String>,
        user: UserHandle,
        replacing: Boolean
      ) {
        sendCurrentApps()
      }
    }

    val handler = Handler(Looper.getMainLooper())
    val registered = runCatching {
      launcherApps?.registerCallback(callback, handler)
    }.isSuccess

    val refreshJob = launch {
      refreshTrigger.collect {
        sendCurrentApps()
      }
    }

    awaitClose {
      if (registered) {
        runCatching { launcherApps?.unregisterCallback(callback) }
      }
      refreshJob.cancel()
    }
  }.flowOn(ioDispatcher)

  override suspend fun refreshApps() {
    refreshTrigger.emit(Unit)
  }

  override fun launchApp(packageName: String): Result<Unit> = runCatching {
    val launchIntent = packageManager.getLeanbackLaunchIntentForPackage(packageName)
      ?: packageManager.getLaunchIntentForPackage(packageName)
      ?: throw IllegalArgumentException("No launchable activity found for package: $packageName")

    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    context.startActivity(launchIntent)
  }

  private fun queryInstalledApps(): List<TvAppInfo> {
    val flags = PackageManager.MATCH_ALL
    val currentPackage = context.packageName

    // 1. Query native Leanback TV apps
    val leanbackIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
    val leanbackApps = packageManager.queryIntentActivities(leanbackIntent, flags)

    // 2. Query standard mobile / sideloaded launcher apps
    val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    val standardApps = packageManager.queryIntentActivities(launcherIntent, flags)

    val leanbackPackageNames = leanbackApps.map { it.activityInfo.packageName }.toSet()

    val tvApps = leanbackApps
      .filter { it.activityInfo.packageName != currentPackage }
      .map { resolveInfo ->
        val pkg = resolveInfo.activityInfo.packageName
        TvAppInfo(
          packageName = pkg,
          activityName = resolveInfo.activityInfo.name,
          label = resolveInfo.loadLabel(packageManager).toString(),
          banner = resolveInfo.activityInfo.loadBanner(packageManager),
          icon = resolveInfo.activityInfo.loadIcon(packageManager),
          isLeanback = true,
        )
      }

    val nonTvApps = standardApps
      .filter {
        it.activityInfo.packageName != currentPackage &&
          it.activityInfo.packageName !in leanbackPackageNames
      }
      .map { resolveInfo ->
        val pkg = resolveInfo.activityInfo.packageName
        TvAppInfo(
          packageName = pkg,
          activityName = resolveInfo.activityInfo.name,
          label = resolveInfo.loadLabel(packageManager).toString(),
          banner = resolveInfo.activityInfo.loadBanner(packageManager),
          icon = resolveInfo.activityInfo.loadIcon(packageManager),
          isLeanback = false,
        )
      }

    return (tvApps + nonTvApps)
      .distinctBy { it.packageName }
      .sortedBy { it.label.lowercase() }
  }
}

/**
 * Test and preview fake implementation of [AppRepository].
 */
class FakeAppRepository(
  initialApps: List<TvAppInfo> = emptyList(),
) : AppRepository {
  private val _apps = MutableStateFlow(initialApps)
  override val apps: Flow<List<TvAppInfo>> = _apps.asStateFlow()

  var lastLaunchedPackage: String? = null
    private set

  override suspend fun refreshApps() {
    // Retain current apps or no-op in tests
  }

  override fun launchApp(packageName: String): Result<Unit> {
    val found = _apps.value.any { it.packageName == packageName }
    return if (found) {
      lastLaunchedPackage = packageName
      Result.success(Unit)
    } else {
      Result.failure(IllegalArgumentException("Package not found: $packageName"))
    }
  }
}
