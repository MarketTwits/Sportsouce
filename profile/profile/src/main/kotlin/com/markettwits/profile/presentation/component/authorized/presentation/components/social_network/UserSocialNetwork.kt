package com.markettwits.profile.presentation.component.authorized.presentation.components.social_network

import InstagramIcon
import WhatsappIcon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.openWebPage
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.image.icons.IconTelegram
import com.markettwits.core_ui.image.icons.IconVk
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile

@Composable
fun UserSocialNetwork(
    modifier: Modifier = Modifier,
    items: UserProfile.SocialNetwork,
    onClickAddSocialNetwork: () -> Unit
) {
    val context = LocalContext.current
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        item {
            AddSocialNetworkButton(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    onClickAddSocialNetwork()
                })
        }
        item {
            items.also {
                if (it.telegram.isNotEmpty()) {
                    SocialNetworkButton(
                        modifier = Modifier.padding(5.dp),
                        icon = IconTelegram,
                        url = items.telegram,
                        color = SportSouceColor.TelegramIcon,
                        onClick = {
                            openWebPage(items.telegram, context)
                        })
                }
                if (it.instagram.isNotEmpty()) {
                    SocialNetworkButton(
                        modifier = Modifier.padding(5.dp),
                        icon = InstagramIcon,
                        url = items.instagram,
                        color = SportSouceColor.InstagramIcon,
                        onClick = {
                            openWebPage(items.instagram, context)
                        })
                }
                if (it.vk.isNotEmpty()) {
                    SocialNetworkButton(
                        modifier = Modifier.padding(5.dp),
                        icon = IconVk,
                        url = items.vk,
                        color = SportSouceColor.VkIcon,
                        onClick = {
                            openWebPage(items.vk, context)
                        })
                }
                if (it.whatsapp.isNotEmpty()) {
                    SocialNetworkButton(
                        modifier = Modifier.padding(5.dp),
                        icon = WhatsappIcon,
                        url = items.whatsapp,
                        color = SportSouceColor.WhatsappIcon,
                        onClick = {
                            openWebPage(items.whatsapp, context)
                        })
                }
            }

        }
    }
}

@Composable
fun SocialNetworkButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    url: String = "",
    color: Color,
    onClick: () -> Unit
) {

    OutlinedButton(
        modifier = modifier
            .size(width = 55.dp, height = 35.dp),
        shape = Shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, color),
        contentPadding = PaddingValues(7.dp),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = icon,
            tint = color,
            contentDescription = ""
        )
    }
}

@Composable
fun AddSocialNetworkButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier
            .size(width = 55.dp, height = 35.dp)
            .clickable { onClick() },
        shape = Shapes.medium,
        contentPadding = PaddingValues(7.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = Icons.Default.Add,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.outline
        )
    }
}