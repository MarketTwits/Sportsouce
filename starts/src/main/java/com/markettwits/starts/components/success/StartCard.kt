package com.markettwits.starts.components.success

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
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
import coil.compose.AsyncImage
import com.markettwits.starts.StartsListItem
import de.charlex.compose.material3.HtmlText

@Composable
fun StartCard(
    start : StartsListItem,
    onItemClick : (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onItemClick(start.id)
            }
    ) {
        Row {
            ImageCard(Modifier.weight(1f), start.image, start.date, start.statusCode)
            ImageCardInfo(Modifier.weight(2f), title = start.name, place = start.place, start.distance)
            ImageInfoButton()
        }
    }
}

@Composable
private fun ImageCard(modifier: Modifier = Modifier, image: String, date: String, status : StartsListItem.StatusCode) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .wrapContentSize()
            .background(Color.LightGray)
            .size(width = 145.dp, height = 170.dp)
    ) {
        AsyncImage(
            model = image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            ImageCardInfoStatus(status)
            ImageCardInfoStroke(date)
        }
    }
}

@Composable
private fun ImageCardInfoStroke(title: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            .fillMaxWidth()
            .background(Color(77,183,254))
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = Color.White,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ImageCardInfoStatus(status: StartsListItem.StatusCode) {
    val backgroundColor = when(status.id){
        3 -> Color(94,207,177)
        2 -> Color(248,189,56)
        6 -> Color.Red
        else -> Color.Blue
    }
    Row(
        modifier = Modifier
            .background(backgroundColor)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = status.message,
            color = Color.White,
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
    Column(modifier = modifier.padding(10.dp)) {
        Text(text = title, fontSize = 16.sp)
        Text(
            text = place,
            fontSize = 10.sp
        )
        HtmlText(text = distance, fontSize = 10.sp)
    }
}

@Composable
private fun ImageInfoButton(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = null,
        tint = Color.Cyan,
        modifier = modifier.size(20.dp)
    )
}