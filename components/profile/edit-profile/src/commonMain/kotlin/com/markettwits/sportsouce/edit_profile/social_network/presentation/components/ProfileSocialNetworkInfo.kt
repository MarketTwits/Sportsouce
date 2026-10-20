package com.markettwits.sportsouce.edit_profile.social_network.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun ProfileSocialNetworkInfo(modifier: Modifier = Modifier) {
    Text("Укажите ссылки на свои социальные сети, чтобы организаторы могли с вами связаться",
        modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
}
