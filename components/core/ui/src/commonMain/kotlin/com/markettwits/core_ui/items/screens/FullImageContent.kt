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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun FullImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    isPortrait: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    val boxModifier = if (isPortrait) modifier
        .fillMaxWidth()
        .height(380.dp) else {
        modifier
    }
    val imageModifier =
        if (isPortrait) Modifier
            .wrapContentSize()
            .width(230.dp)
            .clip(Shapes.large)
        else Modifier.fillMaxSize()
            .clip(Shapes.large)

    Box(
        modifier = boxModifier
            .fillMaxWidth()
            .height(330.dp)
    ) {
        if (isPortrait) {
            Box(modifier = Modifier.fillMaxSize()) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = 6.dp),
                    error = {
                        SubcomposeAsyncImageContent(
                            modifier = modifier,
                            painter = DefaultImages.EmptyImageStart()
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent(modifier = modifier)
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    backgroundColor.copy(alpha = 0.05f),
                                    backgroundColor.copy(alpha = 0.15f),
                                    backgroundColor.copy(alpha = 0.25f),
                                    backgroundColor.copy(alpha = 0.40f),
                                    backgroundColor.copy(alpha = 0.50f),
                                    backgroundColor.copy(alpha = 0.60f),
                                    backgroundColor.copy(alpha = 0.80f),
                                    backgroundColor.copy(alpha = 0.90f),
                                    backgroundColor.copy(alpha = 1.0f)
                                )
                            )
                        )
                )
            }
        }
        Card(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(18.dp)
                .align(Alignment.Center),
            shape = Shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = imageModifier,
                error = {
                    SubcomposeAsyncImageContent(
                        painter = DefaultImages.EmptyImageStart()
                    )
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
        }
    }
}
