package com.markettwits.starts_common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.core_ui.items.text.HtmlText

@Composable
fun StartCard(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    onItemClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 180.dp)
            .padding(10.dp)
            .clip(Shapes.medium)
            .clickable {
                onItemClick(start.id)
            }
    ) {
        Row {
            ImageCard(
                image = start.image,
                date = start.date,
                status = start.statusCode
            )
            ImageCardInfo(
                modifier = Modifier.fillMaxWidth(),
                title = start.name,
                place = start.place,
                distance = start.distance
            )
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: String,
    date: String,
    status: StartsListItem.StatusCode
) {
    Box(
        modifier = modifier
            .size(width = 120.dp, height = 170.dp)
            .clip(Shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), Shapes.medium)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                filterQuality = FilterQuality.Medium,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    if (image.isEmpty())
                        SubcomposeAsyncImageContent(
                            modifier = modifier,
                            painter = DefaultImages.EmptyImageStart()
                        )
                    else
                        Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                loading = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                ImageCardInfoStroke(date)
                ImageCardInfoStatus(status)
            }
        }
    }
}

@Composable
private fun ImageCardInfoStroke(title: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontFamily = FontNunito.bold(),
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ImageCardInfoStatus(status: StartsListItem.StatusCode) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .background(startStatusBackground(status.id))
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = status.message,
            color = Color.White,
            fontFamily = FontNunito.bold(),
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ImageCardInfo(
    modifier: Modifier = Modifier,
    title: String,
    place: String,
    distance: String
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            color = MaterialTheme.colorScheme.outline,
            text = place,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 14.sp,
            fontFamily = FontNunito.medium(),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.padding(4.dp))
        HtmlText(
            text = distance,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}