package com.markettwits.profile.presentation.component.authorized.profile.components.user_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.image.request.imageRequestCrossfade
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.profile.presentation.component.authorized.profile.components.social_network.UserSocialNetwork

@Composable
fun UserInfoCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    userName: String,
    onClickEdit: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Box(modifier = Modifier
            .padding(5.dp)
            .align(Alignment.CenterHorizontally)) {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(120.dp),
                contentScale = ContentScale.Crop,
                model = imageRequestCrossfade(model = imageUrl),
                contentDescription = ""
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = userName,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        val status = ""
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = if (status.isEmpty()) "Статус не установлен" else status,
            fontFamily = FontNunito.regular,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Зарегестрирован(а) 21 декабря 2021 года",
            fontFamily = FontNunito.regular,
            fontSize = 14.sp,
            color = Color.Gray
        )
        ButtonContentBase(
            modifier = Modifier.padding(10.dp),
            title = "Редактировать",
            onClick = { onClickEdit() },
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            content = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Редактировать",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        )
        UserSocialNetwork()

    }
}