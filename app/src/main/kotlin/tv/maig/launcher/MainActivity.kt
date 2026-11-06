package tv.maig.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import tv.maig.launcher.data.DataRepository
import tv.maig.launcher.data.DefaultDataRepository
import tv.maig.launcher.ui.theme.MaigTVTheme

class MainActivity : ComponentActivity() {
  private val dataRepository: DataRepository by lazy { DefaultDataRepository() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val initialRoute: NavKey = NavigationRouteResolver.resolve(intent?.dataString)

    enableEdgeToEdge()
    setContent {
      MaigTVTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainNavigation(dataRepository = dataRepository, initialRoute = initialRoute)
        }
      }
    }
  }
}

/**
 * Resolves incoming deep links and intent URI strings to typed navigation keys.
 */
object NavigationRouteResolver {
  fun resolve(uriString: String?): NavKey {
    if (uriString == null) return Main
    return if (uriString == "maigtv://settings" || uriString.startsWith("maigtv://settings/")) {
      Settings
    } else {
      Main
    }
  }
}
