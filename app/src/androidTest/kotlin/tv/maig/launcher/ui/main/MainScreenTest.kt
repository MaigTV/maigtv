package tv.maig.launcher.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tv.maig.launcher.data.TvAppInfo

/** UI tests for [tv.maig.launcher.ui.main.MainScreen]. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { MainScreen(apps = FAKE_APPS) }
  }

  @Test
  fun allApps_existInLauncherGrid() {
    FAKE_APPS.forEach { app ->
      composeTestRule.onNodeWithText(app.label).assertExists()
    }
  }
}

private val FAKE_APPS = listOf(
  TvAppInfo(
    packageName = "tv.sample.one",
    activityName = "MainActivity",
    label = "Sample App 1",
    isLeanback = true
  ),
  TvAppInfo(
    packageName = "tv.sample.two",
    activityName = "MainActivity",
    label = "Sample App 2",
    isLeanback = true
  ),
)
