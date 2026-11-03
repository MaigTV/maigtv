package tv.maig.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import tv.maig.launcher.core.repository.AppRepository
import tv.maig.launcher.feature.home.HomeScreen
import tv.maig.launcher.feature.home.HomeViewModel

class MainActivity : ComponentActivity() {
    private val appRepository by lazy { AppRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: HomeViewModel = viewModel { HomeViewModel(appRepository, appRepository) }
            MaterialTheme {
                HomeScreen(viewModel = vm)
            }
        }
    }
}
