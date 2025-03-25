package com.markettwits.sportsouce.club.info.presentation.components.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.info.domain.models.Statistic

@Composable
fun StatisticsContent(
    modifier: Modifier = Modifier,
    statistics: List<Statistic>
) {
    Column(modifier = modifier.padding(10.dp)) {
        Text(
            textAlign = TextAlign.Start,
            text = "Наш опыт и немного статистики",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        StatisticContents(statistics = statistics)
    }
}

@Composable
private fun StatisticContents(modifier: Modifier = Modifier, statistics: List<Statistic>) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statistics.forEach {
            StatisticItemContent(statistic = it)
        }
    }
}

@Composable
private fun StatisticItemContent(modifier: Modifier = Modifier, statistic: Statistic) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HtmlText(
            textAlign = TextAlign.Center,
            text = statistic.title,
            fontSize = 24.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            textAlign = TextAlign.Center,
            text = statistic.value,
            fontSize = 20.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}