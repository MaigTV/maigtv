package tv.maig.launcher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import tv.maig.launcher.data.DataRepository
import tv.maig.launcher.ui.main.MainScreen

@Composable
fun MainNavigation(
  dataRepository: DataRepository,
  initialRoute: NavKey = Main
) {
  val backStack = rememberNavBackStack(initialRoute)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainScreen(
            dataRepository = dataRepository,
            onItemClick = { navKey -> backStack.add(navKey) }
          )
        }
        entry<Details> { details ->
          DetailsScreen(
            itemId = details.itemId,
            onBack = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}

@Composable
fun DetailsScreen(
  itemId: String,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Details: $itemId",
        style = MaterialTheme.typography.headlineMedium
      )
      Button(
        modifier = Modifier
          .defaultMinSize(minHeight = 48.dp)
          .semantics { contentDescription = "Back to Home" },
        onClick = onBack
      ) {
        Text("Back to Home")
      }
    }
  }
}
