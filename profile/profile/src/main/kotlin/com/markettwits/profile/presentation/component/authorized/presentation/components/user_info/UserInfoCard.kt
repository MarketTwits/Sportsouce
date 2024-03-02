package com.markettwits.profile.presentation.component.authorized.presentation.components.user_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.image.request.imageRequestCrossfade
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import com.markettwits.profile.presentation.component.authorized.presentation.components.social_network.UserSocialNetwork

@Composable
fun UserInfoCard(
    modifier: Modifier = Modifier,
    user: UserProfile,
    onClickEdit: () -> Unit,
    onClickAddSocialNetwork: () -> Unit,
    onClickImage: () -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .clip(Shapes.small)
                .clickable { onClickImage() }
                .align(Alignment.CenterHorizontally)
        ) {
            SubcomposeAsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(90.dp),
                model = imageRequestCrossfade(model = user.userInfo.photo),
                contentDescription = "",
                success = {
                    SubcomposeAsyncImageContent(
                        modifier = modifier,
                        painter = it.painter
                    )
                },
                error = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.tertiaryContainer)) {
                        Icon(
                            modifier = modifier
                                .size(50.dp)
                                .padding(10.dp)
                                .align(Alignment.Center),
                            imageVector = Icons.Default.Person,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
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

        Button(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 30.dp)
                .height(35.dp),
            onClick = { onClickEdit() },
            border = BorderStroke(0.1.dp, MaterialTheme.colorScheme.tertiary),
            contentPadding = PaddingValues(),
            content = {
                Text(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Редактировать",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
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