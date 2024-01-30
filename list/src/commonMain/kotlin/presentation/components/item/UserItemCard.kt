package presentation.components.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.random_user.RandomUser
import com.seiko.imageloader.rememberImagePainter

@Composable
fun UserItemCard(
    modifier: Modifier = Modifier,
    item: RandomUser,
    onItemClick: (RandomUser) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(height = 180.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onItemClick(item)
            }
    ) {
        Row {
            ImageCard(
                image = item.picture,
            )
            ImageCardInfo(
                modifier = Modifier.fillMaxWidth(),
                name = item.name.full,
                address = "${item.location.country} ${item.location.city} ${item.location.street.name} ${item.location.street.number}",
                phone = item.phone
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
            .size(width = 120.dp, height = 170.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        val painter = rememberImagePainter(image)
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            contentDescription = "image",
        )
    }
}

@Composable
private fun ImageCardInfo(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    phone: String,
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = name,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Text(
            text = address,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Text(
            text = phone,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}
