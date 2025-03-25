package com.markettwits.sportsouce.review.review.presentation.components.archive

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun StartCardSimple(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = modifier.padding(10.dp)) {
        ImageCard(modifier = modifier, start.name, start.image, start.date) {
            onItemClick(start.id)
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
                            painter = DefaultImages.EmptyImageStart()
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
            fontFamily = FontNunito.bold(),
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }

}

@Composable
private fun ImageCardInfoStroke(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = title,
            color = MaterialTheme.colorScheme.onSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontFamily = FontNunito.bold(),
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}
