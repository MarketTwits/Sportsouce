package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.extensions.formatRussianPhone
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.image.social_networks.InstagramIcon
import com.markettwits.core_ui.items.image.social_networks.WhatsappIcon
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile

@Composable
internal fun ProfileInfoTopBar(
    modifier: Modifier = Modifier,
    imageUrl: String,
    userName: String,
    userPhoneNumber: String,
    socialNetwork: UserProfile.SocialNetwork,
    onClickImage: (String) -> Unit,
    onClickEditProfile: () -> Unit,
    onSocialNetworkClick: (String) -> Unit,
    onAddSocialNetwork: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick = onClickEditProfile),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(70.dp),
                model = imageRequestCrossfade(model = imageUrl),
                contentDescription = "",
                success = {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier
                            .clickable {
                                onClickImage(imageUrl)
                            },
                        painter = it.painter
                    )
                },
                error = {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)) {
                        Icon(
                            modifier = Modifier
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
            Spacer(Modifier.width(8.dp))
            Column {
                Row {
                    Text(
                        text = userName,
                        fontFamily = FontNunito.bold(),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.clip(Shapes.medium)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "go to edit profile",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                val phoneNumber = userPhoneNumber.formatRussianPhone()
                if (phoneNumber.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = phoneNumber,
                        fontFamily = FontNunito.medium(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Social Networks Row
        Spacer(Modifier.height(12.dp))
        SocialNetworksFlowRow(
            socialNetwork = socialNetwork,
            onSocialNetworkClick = onSocialNetworkClick,
            onAddSocialNetwork = onAddSocialNetwork
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SocialNetworksFlowRow(
    socialNetwork: UserProfile.SocialNetwork,
    onSocialNetworkClick: (String) -> Unit,
    onAddSocialNetwork: () -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Instagram
        if (socialNetwork.instagram.isNotEmpty()) {
            SocialNetworkBadge(
                icon = InstagramIcon,
                color = SportSouceColor.InstagramIcon,
                onClick = { onSocialNetworkClick(socialNetwork.instagram) }
            )
        }

        // Telegram
        if (socialNetwork.telegram.isNotEmpty()) {
            SocialNetworkBadge(
                icon = IconTelegram,
                color = SportSouceColor.TelegramIcon,
                onClick = { onSocialNetworkClick(socialNetwork.telegram) }
            )
        }

        // VK
        if (socialNetwork.vk.isNotEmpty()) {
            SocialNetworkBadge(
                icon = IconVk,
                color = SportSouceColor.VkIcon,
                onClick = { onSocialNetworkClick(socialNetwork.vk) }
            )
        }

        // WhatsApp
        if (socialNetwork.whatsapp.isNotEmpty()) {
            SocialNetworkBadge(
                icon = WhatsappIcon,
                color = SportSouceColor.WhatsappIcon,
                onClick = {
                    // Format WhatsApp phone number for opening
                    val phoneNumber = socialNetwork.whatsapp.replace("+", "").replace(" ", "")
                    onSocialNetworkClick("https://wa.me/$phoneNumber")
                }
            )
        }

        // Add Social Network Button
        Surface(
            modifier = Modifier
                .height(40.dp)
                .width(48.dp)
                .clip(Shapes.medium)
                .clickable { onAddSocialNetwork() },
            color = MaterialTheme.colorScheme.tertiaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add social network",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun SocialNetworkBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .height(40.dp)
            .width(48.dp)
            .clip(Shapes.medium)
            .clickable { onClick() },
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color),
        shape = Shapes.medium
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
        }
    }
}