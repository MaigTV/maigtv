package tv.maig.launcher.core.model

import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: ImageBitmap?,
    val banner: ImageBitmap?,
    val launchIntent: Intent?
)
