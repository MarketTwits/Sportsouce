package com.markettwits.profile.authorized.presentation.components.user_info.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.R
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.image.request.imageRequestCrossfade
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo

@Composable
fun StartCardSimpleAuth(
    modifier: Modifier = Modifier,
    start: StartOrderInfo,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = modifier.padding(10.dp)) {
        ImageCard(modifier = modifier, start.startTitle, start.image, start.dateStartPreview) {
            onItemClick(start.startId)
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    name: String,
    image: String,
    date: String,
    onCLick: () -> Unit,
) {
    Column(modifier = modifier.size(width = 115.dp, height = 190.dp)) {
        Box(
            modifier = modifier
                .size(width = 115.dp, height = 150.dp)
                .clip(Shapes.medium)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), Shapes.medium)
                .clickable { onCLick() }
        ) {
            Box(modifier = modifier.fillMaxSize()) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(model = image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
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
                Column(modifier = modifier.align(Alignment.BottomCenter)) {
                    ImageCardInfoStroke(
                        modifier = modifier.clip(
                            RoundedCornerShape(
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        ), date
                    )
                }
            }

        }
        Text(
            text = name,
            color = MaterialTheme.colorScheme.tertiary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 10.sp,
            fontFamily = FontNunito.bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }

}

@Composable
private fun ImageCardInfoStroke(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = Modifier
            .background(SportSouceColor.SportSouceLighBlue)
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = title,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontFamily = FontNunito.bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}