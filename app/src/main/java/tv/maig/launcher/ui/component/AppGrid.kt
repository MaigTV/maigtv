package tv.maig.launcher.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CompactCard
import androidx.tv.material3.Text
import tv.maig.launcher.core.model.AppInfo

@Composable
fun AppGrid(apps: List<AppInfo>, onAppClick: (AppInfo) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(apps, key = { it.packageName }) { app ->
            CompactCard(
                onClick = { onAppClick(app) },
                title = { Text(app.label) },
                image = {
                    val imageBitmap = remember(app.packageName) {
                        app.banner ?: app.icon
                    }

                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = app.label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(16f / 9f)
                                .fillMaxWidth()
                        )
                    }

                }
            )
        }
    }
}