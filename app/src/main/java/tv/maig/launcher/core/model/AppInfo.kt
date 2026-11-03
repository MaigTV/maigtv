package tv.maig.launcher.core.model

import android.content.ComponentName
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable

@Immutable
data class AppInfo(
    val packageName: String,
    val componentName: ComponentName,
    val label: String,
    val icon: Drawable?,
)
