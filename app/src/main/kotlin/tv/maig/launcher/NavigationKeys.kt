package tv.maig.launcher

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey

@Serializable data class Details(val itemId: String) : NavKey

