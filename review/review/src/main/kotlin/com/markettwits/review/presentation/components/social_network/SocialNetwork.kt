package com.markettwits.review.presentation.components.social_network

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.openWebPage
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun SocialNetwork(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        SocialNetworkButton(
            modifier = Modifier
                .clickable { onClickVk(context) }
                .weight(1f),
            title = "Мы ВКонтакте",
            textColor = SportSouceColor.VkIcon,
            icon = VkIcon,
            backgroundColor = SportSouceColor.VeryLighBlue
        )
        SocialNetworkButton(
            modifier = Modifier
                .clickable { onClickTelegram(context) }
                .weight(1f),
            title = "Мы в Telegram",
            textColor = SportSouceColor.TelegramIcon,
            icon = IconTelegram,
            backgroundColor = SportSouceColor.VeryLighBlue
        )
    }
}

@Composable
private fun SocialNetworkButton(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    title: String,
    textColor: Color,
    backgroundColor: Color,
) {
    Row(
        modifier = modifier
            .padding(5.dp)
            .clip(Shapes.large)
            .background(backgroundColor)
            .padding(15.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(20.dp),
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Text(
            text = title,
            color = textColor,
            maxLines = 1,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.bold
        )
    }
}

private fun onClickTelegram(context : Context){
    openWebPage("https://t.me/sportsauce", context)
}

private fun onClickVk(context : Context){
    openWebPage("https://vk.com/sportsoyuznsk", context)
}