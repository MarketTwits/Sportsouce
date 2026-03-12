package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.dashboard.presentation.screen.MenuItemColor

@Composable
internal fun ClubMoreSection(
    onFaqClick: () -> Unit,
    onStatisticClick: () -> Unit,
) {
    Column {
        Text(
            text = "Еще",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontFamily = FontNunito.bold()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClubInfoCard(
            title = "Вопросы и ответы",
            description = "Здесь вы найдете ответы на самые часто задаваемые вопросы",
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            colors = MenuItemColor.FAQ.colors,
            onClick = onFaqClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClubInfoCard(
            title = "Наш опыт и немного статистики",
            description = "Чем мы так усердно гордимся)",
            icon = Icons.AutoMirrored.Filled.ShowChart,
            colors = MenuItemColor.STATISTICS.colors,
            onClick = onStatisticClick
        )
    }
}
