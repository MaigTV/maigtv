package tv.maig.launcher.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceColors
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    colors: SurfaceColors = SurfaceDefaults.colors(
        containerColor = Color.Black,
        contentColor = Color.White,
    ),
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(modifier = modifier.fillMaxSize(), colors = colors, content = content)
}

@Preview
@Composable
private fun ScreenPreview() {
    MaterialTheme {
        Screen {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Screen Preview",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}