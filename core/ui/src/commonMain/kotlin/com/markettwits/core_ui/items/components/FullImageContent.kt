package com.markettwits.core_ui.items.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun FullImageContent(modifier: Modifier = Modifier, imageUrl: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
            error = {
//                SubcomposeAsyncImageContent(
//                    modifier = modifier,
//                    painter = painterResource(id = R.drawable.default_start_image)
//                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
        Card(
            modifier = Modifier
                .padding(18.dp)
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .wrapContentSize()
                    .width(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
                error = {
//                    SubcomposeAsyncImageContent(
//                        modifier = modifier,
//                        painter = painterResource(id = R.drawable.default_start_image)
//                    )
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
        }
    }
}
