package tv.maig.launcher.feature.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tv.maig.launcher.core.repositories.AppRepository
import tv.maig.launcher.models.AppInfo

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository: AppRepository = AppRepository(application.applicationContext)
    private val _apps: MutableStateFlow<List<AppInfo>> = MutableStateFlow(emptyList())
    var apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _apps.value = appRepository.getInstalledApps()
        }
    }

    fun launchApp(context: Context, app: AppInfo) {
        app.launchIntent?.let { intent ->
            context.startActivity(intent)
        }
    }
}