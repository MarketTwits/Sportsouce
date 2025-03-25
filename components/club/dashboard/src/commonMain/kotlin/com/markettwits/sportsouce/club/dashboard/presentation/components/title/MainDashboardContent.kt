package com.markettwits.sportsouce.club.dashboard.presentation.components.title

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MainDashboardContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        MainDashboardImage()
        MainDashboardTitle(
            title = "Спортивный клуб",
            description = "Комплексный подход во всем - от дружной команды до собственных спортивных мероприятий"
        )
    }
}