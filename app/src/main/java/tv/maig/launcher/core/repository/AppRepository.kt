package tv.maig.launcher.core.repository

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.UserHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import tv.maig.launcher.core.model.AppInfo
import kotlin.time.Duration.Companion.milliseconds

interface InstalledAppsRepository {
    suspend fun getInstalledApp(): List<AppInfo>

    fun observeInstalledApps(): Flow<List<AppInfo>>
}

interface AppLauncher {
    fun launchApp(app: AppInfo)
}

class AppRepository(private val context: Context) :
    InstalledAppsRepository,
    AppLauncher {
    private val launcherApps = context.getSystemService(LauncherApps::class.java)
    private val user = Process.myUserHandle()

    override fun launchApp(app: AppInfo) {
        try {
            launcherApps.startMainActivity(
                app.componentName,
                user,
                null,
                null,
            )
        } catch (_: SecurityException) {
            // Ignore
        }
    }

    override suspend fun getInstalledApp(): List<AppInfo> = withContext(Dispatchers.IO) {
        launcherApps.getActivityList(null, user)
            .distinctBy { it.componentName.packageName }
            .filter { it.componentName.packageName != context.packageName }
            .map { info ->
                AppInfo(
                    packageName = info.componentName.packageName,
                    componentName = info.componentName,
                    label = info.label.toString(),
                    icon = info.getIcon(context.resources.displayMetrics.densityDpi),
                )
            }
            .sortedBy { it.label.lowercase() }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun observeInstalledApps(): Flow<List<AppInfo>> = callbackFlow {
        val refreshSignal: () -> Unit = { trySend(Unit) }
        val callback = object : LauncherApps.Callback() {
            override fun onPackageAdded(packageName: String, user: UserHandle) = refreshSignal()
            override fun onPackageRemoved(packageName: String, user: UserHandle) = refreshSignal()
            override fun onPackageChanged(packageName: String, user: UserHandle) = refreshSignal()

            override fun onPackagesAvailable(
                packageNames: Array<out String>,
                user: UserHandle,
                replacing: Boolean,
            ) = refreshSignal()

            override fun onPackagesUnavailable(
                packageNames: Array<out String>,
                user: UserHandle,
                replacing: Boolean,
            ) = refreshSignal()

            override fun onPackagesSuspended(
                packageNames: Array<out String>,
                user: UserHandle,
            ) = refreshSignal()

            override fun onPackagesUnsuspended(
                packageNames: Array<out String>,
                user: UserHandle,
            ) = refreshSignal()
        }

        launcherApps.registerCallback(callback, Handler(Looper.getMainLooper()))
        refreshSignal()
        awaitClose { launcherApps.unregisterCallback(callback) }
    }
        .debounce(150.milliseconds)
        .mapLatest { getInstalledApp() }
}
