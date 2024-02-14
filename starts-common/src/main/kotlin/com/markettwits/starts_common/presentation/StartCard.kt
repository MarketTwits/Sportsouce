package com.markettwits.starts_common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
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
import com.markettwits.starts_common.domain.StartsListItem
import de.charlex.compose.material3.HtmlText

@Composable
fun StartCard(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    onItemClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.White)
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
            ImageInfoButton()
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
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), Shapes.medium)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
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
            .background(SportSouceColor.SportSouceLighBlue)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
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
            fontFamily = FontNunito.bold,
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
            fontFamily = FontNunito.bold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            color = SportSouceColor.SportSouceBlue,
            text = place,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontFamily = FontNunito.medium,
            fontSize = 12.sp
        )
        HtmlText(
            text = distance,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
    }
}

@Composable
private fun ImageInfoButton(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = null,
        tint = SportSouceColor.SportSouceBlue,
        modifier = modifier.size(20.dp)
    )
}