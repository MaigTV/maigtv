package tv.maig.launcher.models

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: ImageBitmap?,
    val banner: ImageBitmap?,
    val launchIntent: Intent?
)
