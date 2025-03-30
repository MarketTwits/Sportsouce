package com.markettwits.sportsouce.profile.registrations.simple_list

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo


@Composable
fun StartOrderSimpleCard(
    modifier: Modifier = Modifier,
    start: StartOrderInfo,
    onItemClick: (StartOrderInfo) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(Shapes.medium)
            .clickable {
                onItemClick(start)
            }
    ) {
        Row {
            ImageCard(
                image = start.image,
            )
            ImageCardInfo(
                modifier = Modifier.padding(start = 10.dp),
                title = start.startTitle,
                dateStart = start.dateStartPreview,
                kindOfSport = start.payment.title
            )
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier
            .size(width = 70.dp, height = 90.dp)
            .clip(Shapes.medium)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
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
        }
    }
}

@Composable
private fun ImageCardInfo(
    modifier: Modifier = Modifier,
    title: String,
    dateStart : String,
    kindOfSport : String,
) {
    Column(
        modifier = Modifier
    ) {
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 16.sp,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = modifier,
            text = dateStart,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 14.sp,
            fontFamily = FontNunito.medium(),
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = modifier,
            text = kindOfSport,
            color = MaterialTheme.colorScheme.outline,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 14.sp,
            fontFamily = FontNunito.medium(),
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }
}