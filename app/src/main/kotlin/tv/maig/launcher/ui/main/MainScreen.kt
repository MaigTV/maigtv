package tv.maig.launcher.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.maig.launcher.R
import tv.maig.launcher.data.AppRepository
import tv.maig.launcher.data.TvAppInfo
import tv.maig.launcher.ui.theme.MaigTVTheme

@Composable
fun MainScreen(
  appRepository: AppRepository,
  onSettingsClick: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(appRepository) },
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  when (val state = uiState) {
    MainScreenUiState.Loading -> {
      Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }
    }
    is MainScreenUiState.Success -> {
      MainScreen(
        apps = state.apps,
        onAppClick = { packageName -> viewModel.launchApp(packageName) },
        onSettingsClick = onSettingsClick,
        modifier = modifier
      )
    }
    is MainScreenUiState.Error -> {
      Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text("Error loading launcher: ${state.throwable.message}")
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
  apps: List<TvAppInfo>,
  modifier: Modifier = Modifier,
  onAppClick: (String) -> Unit = {},
  onSettingsClick: () -> Unit = {},
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
          Button(
            modifier = Modifier
              .defaultMinSize(minHeight = 48.dp)
              .semantics { contentDescription = "Open Settings" },
            onClick = onSettingsClick
          ) {
            Text("Settings")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (apps.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No TV applications installed",
            style = MaterialTheme.typography.bodyLarge
          )
        }
      } else {
        apps.forEach { app ->
          Button(
            modifier = Modifier
              .defaultMinSize(minHeight = 48.dp)
              .semantics { contentDescription = "Launch ${app.label}" },
            onClick = { onAppClick(app.packageName) }
          ) {
            Text(text = app.label, style = MaterialTheme.typography.bodyLarge)
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  MaigTVTheme {
    MainScreen(
      apps = listOf(
        TvAppInfo("com.google.android.youtube.tv", "MainActivity", "YouTube"),
        TvAppInfo("com.netflix.ninja", "MainActivity", "Netflix"),
        TvAppInfo("com.amazon.amazonvideo.livingroom", "MainActivity", "Prime Video")
      )
    )
  }
}
