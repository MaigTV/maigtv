package tv.maig.launcher

import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationRouteTest {

  @Test
  fun resolve_returnsMainWhenUriIsNull() {
    val route = NavigationRouteResolver.resolve(null)
    assertEquals(Main, route)
  }

  @Test
  fun resolve_returnsMainWhenUriIsUnrecognized() {
    val route = NavigationRouteResolver.resolve("https://example.com")
    assertEquals(Main, route)
  }

  @Test
  fun resolve_returnsDetailsWhenDeepLinkMatches() {
    val route = NavigationRouteResolver.resolve("maigtv://details/settings")
    assertEquals(Details("settings"), route)
  }
}
