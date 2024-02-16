package com.markettwits.profile.presentation.component.authorized.profile.components.user_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.image.icons.IconTelegram
import com.markettwits.core_ui.image.icons.IconVk
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun UserSocialNetwork(modifier: Modifier = Modifier) {
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
                })
            SocialNetworkButton(
                modifier = Modifier.padding(5.dp),
                icon = IconTelegram,
                url = "",
                color = SportSouceColor.TelegramIcon,
                onClick = {})
            SocialNetworkButton(
                modifier = Modifier.padding(5.dp),
                icon = IconVk,
                url = "",
                color = SportSouceColor.VkIcon,
                onClick = {})
            SocialNetworkButton(
                modifier = Modifier.padding(5.dp),
                icon = IconTelegram,
                url = "",
                color = SportSouceColor.TelegramIcon,
                onClick = {})
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
    Card(
        modifier = modifier
            .size(width = 50.dp, height = 30.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, color)
    ) {
        Icon(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            imageVector = icon,
            tint = color,
            contentDescription = ""
        )
    }
}

@Composable
fun AddSocialNetworkButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .size(width = 50.dp, height = 30.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
    ) {
        Icon(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            imageVector = Icons.Default.Add,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}