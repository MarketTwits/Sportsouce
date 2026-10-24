package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
internal fun ProfileActionCards(
    modifier: Modifier = Modifier,
    onClickFavorites: () -> Unit,
    onClickMembers: () -> Unit,
    onClickResults: () -> Unit,
) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(8.dp)) {
            SauceActionRow("Мои результаты", onClickResults, icon = Icons.Outlined.EmojiEvents)
            HorizontalDivider(color = SauceTheme.colors.border, modifier = Modifier.padding(horizontal = 8.dp))
            SauceActionRow("Избранное", onClickFavorites, icon = Icons.Default.FavoriteBorder)
            HorizontalDivider(color = SauceTheme.colors.border, modifier = Modifier.padding(horizontal = 8.dp))
            SauceActionRow("Участники", onClickMembers, icon = Icons.Default.Groups)
        }
    }
}

internal fun StartOrderPaymentStatus.profileTone(): SauceStatusTone = when (this) {
    is StartOrderPaymentStatus.Success, is StartOrderPaymentStatus.Free -> SauceStatusTone.Success
    is StartOrderPaymentStatus.NotPaid -> SauceStatusTone.Warning
    is StartOrderPaymentStatus.PaymentCancelled -> SauceStatusTone.Danger
    is StartOrderPaymentStatus.OnPlace, is StartOrderPaymentStatus.WithoutStatus -> SauceStatusTone.Neutral
}
