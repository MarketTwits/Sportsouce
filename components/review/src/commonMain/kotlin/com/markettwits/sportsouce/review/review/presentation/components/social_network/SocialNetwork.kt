package com.markettwits.sportsouce.review.review.presentation.components.social_network


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun SocialNetwork(
    modifier: Modifier = Modifier,
    onClickTelegram: () -> Unit,
    onClickVk: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialNetworkButton(
            modifier = Modifier.weight(1f),
            title = "Мы ВКонтакте",
            textColor = SportSouceColor.VkIcon,
            icon = IconVk,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = {
                onClickVk()
            }
        )
        SocialNetworkButton(
            modifier = Modifier.weight(1f),
            title = "Мы в Telegram",
            textColor = SportSouceColor.TelegramIcon,
            icon = IconTelegram,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = {
                onClickTelegram()
            }
        )
    }
}

@Composable
private fun SocialNetworkButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(5.dp)
            .clip(Shapes.large)
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(20.dp),
            imageVector = icon,
            contentDescription = title,
            tint = textColor
        )
        Text(
            text = title,
            color = textColor,
            maxLines = 1,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.bold()
        )
    }
}