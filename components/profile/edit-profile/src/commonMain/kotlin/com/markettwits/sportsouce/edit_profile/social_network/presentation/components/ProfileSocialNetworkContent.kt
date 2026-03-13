package com.markettwits.sportsouce.edit_profile.social_network.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.markettwits.sportsouce.edit_profile.social_network.domain.UserSocialNetwork

@Composable
fun ProfileSocialNetworkContent(
    modifier: Modifier = Modifier,
    user: UserSocialNetwork,
    onUserChange: (UserSocialNetwork) -> Unit
) {
    // Keyboard and focus management
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Focus requesters for navigation between fields
    val telegramFocusRequester = remember { FocusRequester() }
    val whatsAppFocusRequester = remember { FocusRequester() }
    val vkFocusRequester = remember { FocusRequester() }
    val instagramFocusRequester = remember { FocusRequester() }
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        val localModifier = Modifier.padding(8.dp)
        Column(modifier = localModifier) {
            val label = "Ссылка или никнейм"
            SocialNetworkRaw(
                modifier = localModifier,
                label = "Telegram",
                icon = IconTelegram,
                color = SportSouceColor.TelegramIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier.focusRequester(telegramFocusRequester),
                value = user.telegram,
                onValueChange = { newValue -> onUserChange(user.copy(telegram = newValue)) },
                label = label,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        whatsAppFocusRequester.requestFocus()
                    }
                )
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "WhatsApp",
                icon = WhatsappIcon,
                color = SportSouceColor.WhatsappIcon
            )
            OutlinePhoneTextFiled(
                modifier = localModifier.focusRequester(whatsAppFocusRequester),
                value = user.whatsApp,
                onValueChange = { newValue -> onUserChange(user.copy(whatsApp = newValue)) },
                label = "Номер телефона",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        vkFocusRequester.requestFocus()
                    }
                )
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "ВКонтакте",
                icon = IconVk,
                color = SportSouceColor.VkIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier.focusRequester(vkFocusRequester),
                value = user.vk,
                onValueChange = { newValue -> onUserChange(user.copy(vk = newValue)) },
                label = label,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        instagramFocusRequester.requestFocus()
                    }
                )
            )
            SocialNetworkRaw(
                modifier = localModifier,
                label = "Instagram",
                icon = InstagramIcon,
                color = SportSouceColor.InstagramIcon
            )
            OutlinedTextFieldBase(
                modifier = localModifier.focusRequester(instagramFocusRequester),
                value = user.instagram,
                onValueChange = { newValue -> onUserChange(user.copy(instagram = newValue)) },
                label = label,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
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