package com.markettwits.start.presentation.start.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.start.R
import io.ktor.client.utils.EmptyContent.status

@Composable
fun StartImage(modifier: Modifier = Modifier, imageUrl: String) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            painter = painterResource(id = com.markettwits.core_ui.R.drawable.default_start_image),
            contentDescription = "start image",
            alpha = 0.3f
        )
//        Image(
//            modifier = modifier
//                .fillMaxSize()
//                .align(Alignment.TopCenter)
//                .clip(RoundedCornerShape(10.dp)),
//            painter = painterResource(id = com.markettwits.core_ui.R.drawable.default_start_image),
//            contentDescription = "start image",
//        )
    }
}

@Composable
fun CustomScreen(modifier: Modifier = Modifier, imageUrl: String) {
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
                SubcomposeAsyncImageContent(
                    modifier = modifier,
                    painter = painterResource(id = R.drawable.default_start_image)
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
//        AsyncImage(
//            model = imageUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxSize()
//                .alpha(0.2f)
//
//        )
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
                    SubcomposeAsyncImageContent(
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.default_start_image)
                    )
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
//            AsyncImage(
//                model = imageUrl,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .width(200.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
        }

    }
}
