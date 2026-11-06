package tv.maig.launcher.ui.main

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tv.maig.launcher.data.FakeDataRepository

class MainScreenViewModelTest {

  @Test
  fun uiState_initiallyEmitsSuccessWithData() = runTest {
    val sampleApps = listOf("TV App A", "TV App B")
    val repository = FakeDataRepository(sampleApps)
    val viewModel = MainScreenViewModel(repository)

    val state = viewModel.uiState.first { it !is MainScreenUiState.Loading }
    assertTrue(state is MainScreenUiState.Success)
    assertEquals(sampleApps, (state as MainScreenUiState.Success).data)
  }
}
