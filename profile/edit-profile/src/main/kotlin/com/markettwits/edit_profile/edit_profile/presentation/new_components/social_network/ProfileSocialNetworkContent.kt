package com.markettwits.edit_profile.edit_profile.presentation.new_components.social_network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.TextFieldBase
import com.markettwits.core_ui.image.icons.IconTelegram
import com.markettwits.core_ui.image.icons.IconVk
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage

@Composable
fun ProfileSocialNetworkContent(
    modifier: Modifier = Modifier,
    user: EditProfileUiPage.MySocialNetwork,
    onUserChange: (EditProfileUiPage.MySocialNetwork) -> Unit
) {
    Column(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp)
    ) {
        val localModifier = Modifier.padding(5.dp)
        SocialNetworkRaw(
            modifier = localModifier,
            label = "Telegram",
            icon = IconTelegram,
            color = SportSouceColor.TelegramIcon
        )
        TextFieldBase(
            modifier = localModifier,
            value = user.telegram,
            onValueChange = { newValue -> onUserChange(user.copy(telegram = newValue)) },
            label = "Telegram"
        )
        SocialNetworkRaw(
            modifier = localModifier,
            label = "WhatsApp",
            icon = IconVk,
            SportSouceColor.WhatsappIcon
        )
        TextFieldBase(
            modifier = localModifier,
            value = user.whatsApp,
            onValueChange = { newValue -> onUserChange(user.copy(whatsApp = newValue)) },
            label = "WhatsApp"
        )
        SocialNetworkRaw(
            modifier = localModifier,
            label = "ВКонтакте",
            icon = IconVk,
            color = SportSouceColor.VkIcon
        )
        TextFieldBase(
            modifier = localModifier,
            value = user.vk,
            onValueChange = { newValue -> onUserChange(user.copy(vk = newValue)) },
            label = "ВКонтакте"
        )
        SocialNetworkRaw(
            modifier = localModifier,
            label = "Instagram",
            icon = IconTelegram,
            color = SportSouceColor.InstagramIcon
        )
        TextFieldBase(
            modifier = localModifier,
            value = user.instagram,
            onValueChange = { newValue -> onUserChange(user.copy(instagram = newValue)) },
            label = "Instagram"
        )
    }
}

@Composable
private fun SocialNetworkRaw(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = icon,
            contentDescription = "",
            tint = color
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(text = label, fontFamily = FontNunito.bold, fontSize = 14.sp, color = color)
    }
}