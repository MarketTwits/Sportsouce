package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.image.DefaultImages

@Composable
fun FullImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    isPortrait: Boolean = true
) {
    val boxModifier = if (isPortrait) modifier
        .fillMaxWidth()
        .height(350.dp) else {
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
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .blur(radius = 4.dp)
                    .fillMaxSize()
                    .alpha(0.15f),
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
        }
        Card(
            modifier = Modifier
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
