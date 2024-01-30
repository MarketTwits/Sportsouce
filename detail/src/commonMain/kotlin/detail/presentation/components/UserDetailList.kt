package detail.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import com.markettwits.random_user.RandomUser
import com.seiko.imageloader.rememberImagePainter

@Composable
fun UserDetailList(
    modifier: Modifier = Modifier,
    user: RandomUser,
    onClickEmail: (String) -> Unit,
    onClickMap: (String) -> Unit,
    onClickGeoMap: (String) -> Unit,
    onClickPhone: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            val painter = rememberImagePainter(user.picture)
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .size(220.dp)
                    .border(
                        BorderStroke(2.dp, Color.LightGray),
                        RoundedCornerShape(20.dp)
                    ),
                contentDescription = "image",
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                user.name.full,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Birthday: ${user.ageParam.date}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        onClickEmail(user.email)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Location: ${user.location.coordinates.longitude},${user.location.coordinates.latitude}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .clickable(
                        onClick = {
                            onClickMap("${user.location.coordinates.longitude},${user.location.coordinates.latitude}")
                        }
                    )
                    .widthIn(0.dp, 300.dp),
                textAlign = TextAlign.Center,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Geo: ${user.location.coordinates.latitude}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        onClickGeoMap(user.location.coordinates.latitude)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                "Phone: ${user.phone}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.clickable(
                    onClick = {
                        onClickPhone(user.phone)
                    }
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}