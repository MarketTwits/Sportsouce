package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun FullImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    isPortrait: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    backdropScrollOffset: () -> Int = { 0 },
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val posterWidth = (maxWidth * .56f).coerceIn(0.dp, 320.dp)
        val imageModifier = if (isPortrait) Modifier
            .width(posterWidth)
            .aspectRatio(2f / 3f)
            .clip(Shapes.large)
        else Modifier.fillMaxSize().clip(Shapes.large)
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val frameModifier = if (isPortrait) Modifier.fillMaxWidth()
            .height(posterWidth * 1.5f + statusBarPadding + 60.dp)
        else Modifier.fillMaxSize().height(330.dp)
        Box(frameModifier.clip(RectangleShape)) {
            if (isPortrait) {
                Box(Modifier.matchParentSize()) {
                    SubcomposeAsyncImage(
                        model = imageRequestCrossfade(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val fraction = (backdropScrollOffset() / size.height.coerceAtLeast(1f)).coerceIn(0f, 1f)
                                translationY = size.height * fraction * 0.16f
                                alpha = 1f - fraction * 0.55f
                            }
                            .blur(radius = 4.dp),
                        error = {
                            SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart())
                        },
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    0f to backgroundColor.copy(alpha = .68f),
                                    .45f to backgroundColor.copy(alpha = .72f),
                                    .77f to backgroundColor.copy(alpha = .89f),
                                    .98f to backgroundColor,
                                )
                            )
                    )
                }
            }
            Card(
                modifier = if (isPortrait) Modifier.align(Alignment.TopCenter).padding(top = statusBarPadding + 52.dp)
                    else Modifier.align(Alignment.Center).windowInsetsPadding(WindowInsets.statusBars).padding(18.dp),
                shape = Shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier,
                    error = {
                        SubcomposeAsyncImageContent(
                            painter = DefaultImages.EmptyImageStart()
                        )
                    },
                )
            }
        }
    }
}
