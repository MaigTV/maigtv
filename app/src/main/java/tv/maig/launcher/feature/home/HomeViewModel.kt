package tv.maig.launcher.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tv.maig.launcher.core.model.AppInfo
import tv.maig.launcher.core.repository.AppLauncher
import tv.maig.launcher.core.repository.InstalledAppsRepository

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(val apps: List<AppInfo>) : HomeUiState
}

class HomeViewModel(
    installedAppsRepository: InstalledAppsRepository,
    private val appLauncher: AppLauncher,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = installedAppsRepository.observeInstalledApps()
        .map<List<AppInfo>, HomeUiState>(HomeUiState::Content)
        .catch { emit(HomeUiState.Loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    fun launchApp(app: AppInfo) = appLauncher.launchApp(app)
}
