package tv.maig.launcher.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import tv.maig.launcher.core.model.AppInfo
import tv.maig.launcher.feature.home.HomeUiState.Loading
import tv.maig.launcher.ui.component.AppGrid

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState, onAppClick = viewModel::launchApp)
}

@Composable
fun HomeScreen(uiState: HomeUiState, onAppClick: (AppInfo) -> Unit) {
    when (uiState) {
        Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "MaigTV")
        }

        is HomeUiState.Content -> AppGrid(uiState.apps, onAppClick = onAppClick)
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(uiState = Loading, onAppClick = {})
    }
}
