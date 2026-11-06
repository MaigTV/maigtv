package tv.maig.launcher.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tv.maig.launcher.data.AppRepository
import tv.maig.launcher.data.TvAppInfo

class MainScreenViewModel(
  private val appRepository: AppRepository
) : ViewModel() {

  val uiState: StateFlow<MainScreenUiState> =
    appRepository.apps
      .map<List<TvAppInfo>, MainScreenUiState>(MainScreenUiState::Success)
      .catch { emit(MainScreenUiState.Error(it)) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState.Loading
      )

  fun launchApp(packageName: String): Result<Unit> {
    return appRepository.launchApp(packageName)
  }
}

sealed interface MainScreenUiState {
  data object Loading : MainScreenUiState

  data class Error(val throwable: Throwable) : MainScreenUiState

  data class Success(val apps: List<TvAppInfo>) : MainScreenUiState
}
