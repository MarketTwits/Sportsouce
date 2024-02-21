package com.markettwits.profile.presentation.component.authorized.presentation.components.user_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.image.request.imageRequestCrossfade
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import com.markettwits.profile.presentation.component.authorized.presentation.components.social_network.UserSocialNetwork

@Composable
fun UserInfoCard(
    modifier: Modifier = Modifier,
    user: UserProfile,
    onClickEdit: () -> Unit,
    onClickAddSocialNetwork: () -> Unit
) {
    OnBackgroundCard(modifier = modifier) {

        Box(modifier = Modifier
            .padding(5.dp)
            .align(Alignment.CenterHorizontally)) {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(120.dp),
                contentScale = ContentScale.Crop,
                model = imageRequestCrossfade(model = user.userInfo.photo),
                contentDescription = ""
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = user.userInfo.fullName(),
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = user.userInfo.status.ifEmpty { "Статус не установлен" },
            fontFamily = FontNunito.regular,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.tertiary
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
        UserSocialNetwork(
            items = user.socialNetwork,
            onClickAddSocialNetwork = { onClickAddSocialNetwork() })

    }
}