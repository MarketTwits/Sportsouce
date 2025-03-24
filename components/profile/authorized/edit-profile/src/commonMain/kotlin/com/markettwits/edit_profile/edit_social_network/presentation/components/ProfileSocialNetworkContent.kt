package com.markettwits.edit_profile.edit_social_network.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.image.social_networks.InstagramIcon
import com.markettwits.core_ui.items.image.social_networks.WhatsappIcon
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork

@Composable
fun ProfileSocialNetworkContent(
    modifier: Modifier = Modifier,
    user: UserSocialNetwork,
    onUserChange: (UserSocialNetwork) -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        val localModifier = Modifier.padding(5.dp)
        Column(modifier = localModifier) {
            val label = "Ссылка или никнейм"
            SocialNetworkRaw(
                modifier = localModifier,
                label = "Telegram",
                icon = IconTelegram,
                color = SportSouceColor.TelegramIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier,
                value = user.telegram,
                onValueChange = { newValue -> onUserChange(user.copy(telegram = newValue)) },
                label = label
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "WhatsApp",
                icon = WhatsappIcon,
                color = SportSouceColor.WhatsappIcon
            )
            OutlinePhoneTextFiled(
                modifier = localModifier,
                value = user.whatsApp,
                onValueChange = { newValue -> onUserChange(user.copy(whatsApp = newValue)) },
                label = "Номер телефона"
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "ВКонтакте",
                icon = IconVk,
                color = SportSouceColor.VkIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier,
                value = user.vk,
                onValueChange = { newValue -> onUserChange(user.copy(vk = newValue)) },
                label = label
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "Instagram",
                icon = InstagramIcon,
                color = SportSouceColor.InstagramIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier,
                value = user.instagram,
                onValueChange = { newValue -> onUserChange(user.copy(instagram = newValue)) },
                label = label
            )
        }
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
        Text(text = label, fontFamily = FontNunito.bold(), fontSize = 14.sp, color = color)
    }
}