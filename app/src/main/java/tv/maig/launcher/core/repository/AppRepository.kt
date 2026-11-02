package tv.maig.launcher.core.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.maig.launcher.core.model.AppInfo

class AppRepository(private val context: Context) {
    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val maigPkgName = context.packageName

        val leanbackIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }
        val launcherIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val leanbackApps = getIntentApps(leanbackIntent, pm)
        val launcherApps = getIntentApps(launcherIntent, pm)

        val combinedApps = (leanbackApps + launcherApps)
            .filter {
                it.activityInfo.packageName != maigPkgName
            }
            .distinctBy {
                it.activityInfo.packageName
            }

        combinedApps
            .mapNotNull { resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName
                val label = resolveInfo.loadLabel(pm).toString()

                val launchIntent =
                    pm.getLeanbackLaunchIntentForPackage(packageName)
                        ?: pm.getLaunchIntentForPackage(packageName)

                if (launchIntent == null) return@mapNotNull null

                val iconDrawable = resolveInfo.loadIcon(pm)
                val bannerDrawable = resolveInfo.activityInfo.loadBanner(pm)

                AppInfo(
                    packageName = packageName,
                    label = label,
                    icon = iconDrawable?.toImageBitmap(),
                    banner = bannerDrawable?.toImageBitmap(),
                    launchIntent = launchIntent
                )
            }
            .sortedBy { it.label.lowercase() }
    }

    private fun getIntentApps(
        intent: Intent,
        pm: PackageManager,
        flags: Int = 0
    ): List<ResolveInfo> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return pm.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(flags.toLong())
            )
        }

        return pm.queryIntentActivities(intent, flags)
    }

    private fun Drawable.toImageBitmap(): ImageBitmap? {
        return try {
            if (this is BitmapDrawable && this.bitmap != null) {
                return this.bitmap.asImageBitmap()
            }
            val width = if (intrinsicWidth > 0) intrinsicWidth else 128
            val height = if (intrinsicHeight > 0) intrinsicHeight else 128
            val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}