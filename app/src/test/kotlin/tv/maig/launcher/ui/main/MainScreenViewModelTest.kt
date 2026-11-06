package tv.maig.launcher.ui.main

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tv.maig.launcher.data.FakeAppRepository
import tv.maig.launcher.data.TvAppInfo

class MainScreenViewModelTest {

  @Test
  fun uiState_initiallyEmitsSuccessWithApps() = runTest {
    val sampleApps = listOf(
      TvAppInfo(
        packageName = "com.google.android.youtube.tv",
        activityName = "com.google.android.youtube.tv.MainActivity",
        label = "YouTube TV",
        isLeanback = true
      ),
      TvAppInfo(
        packageName = "com.netflix.ninja",
        activityName = "com.netflix.ninja.MainActivity",
        label = "Netflix",
        isLeanback = true
      )
    )
    val repository = FakeAppRepository(sampleApps)
    val viewModel = MainScreenViewModel(repository)

    val state = viewModel.uiState.first { it !is MainScreenUiState.Loading }
    assertTrue(state is MainScreenUiState.Success)
    assertEquals(sampleApps, (state as MainScreenUiState.Success).apps)
  }

  @Test
  fun launchApp_delegatesToRepository() = runTest {
    val app = TvAppInfo(
      packageName = "com.plexapp.android",
      activityName = "com.plexapp.android.MainActivity",
      label = "Plex",
      isLeanback = true
    )
    val repository = FakeAppRepository(listOf(app))
    val viewModel = MainScreenViewModel(repository)

    val result = viewModel.launchApp(app.packageName)
    assertTrue(result.isSuccess)
    assertEquals("com.plexapp.android", repository.lastLaunchedPackage)
  }

  @Test
  fun launchApp_returnsFailureForNonExistentPackage() = runTest {
    val repository = FakeAppRepository(emptyList())
    val viewModel = MainScreenViewModel(repository)

    val result = viewModel.launchApp("com.nonexistent.app")
    assertTrue(result.isFailure)
  }
}
