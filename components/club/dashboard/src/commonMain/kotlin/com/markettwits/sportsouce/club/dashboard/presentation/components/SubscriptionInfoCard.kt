package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import com.markettwits.sportsouce.club.dashboard.presentation.screen.MenuItemColor

@Composable
internal fun SubscriptionInfoCard(onClick: () -> Unit) {
    ClubInfoCard(
        title = "Абонементы и цены",
        description = "Наша команда — мастера спорта и опытные тренеры, чемпионы и призёры России и мира по лыжным гонкам, плаванию и полиатлону.",
        icon = Icons.Default.CreditCard,
        colors = MenuItemColor.SUBSCRIPTIONS.colors,
        onClick = onClick
    )
}