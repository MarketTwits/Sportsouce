package com.markettwits.sportsouce.review.review.presentation.components.social_network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.image.social_networks.IconTelegram
import com.markettwits.core_ui.items.image.social_networks.IconVk
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.review.review.presentation.components.review_menu.ReviewMenuButton
import com.markettwits.sportsouce.review.review.presentation.components.review_menu.reviewMenuShape

@Composable
fun SocialNetwork(
    modifier: Modifier = Modifier,
    onClickTelegram: () -> Unit,
    onClickVk: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = SauceSpacing.medium),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReviewMenuButton(
            modifier = Modifier.weight(1f),
            title = "Мы ВКонтакте",
            icon = IconVk,
            accentColor = SportSouceColor.VkIcon,
            backgroundColor = SportSouceColor.VkIcon.copy(alpha = 0.08f).compositeOver(SauceTheme.colors.surface),
            shape = reviewMenuShape(0, 2),
            onClick = onClickVk,
        )
        ReviewMenuButton(
            modifier = Modifier.weight(1f),
            title = "Мы в Telegram",
            icon = IconTelegram,
            accentColor = SportSouceColor.TelegramIcon,
            backgroundColor = SportSouceColor.TelegramIcon.copy(alpha = 0.08f).compositeOver(SauceTheme.colors.surface),
            shape = reviewMenuShape(1, 2),
            onClick = onClickTelegram,
        )
    }
}
