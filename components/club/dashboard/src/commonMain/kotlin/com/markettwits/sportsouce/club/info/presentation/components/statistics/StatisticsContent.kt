package com.markettwits.sportsouce.club.info.presentation.components.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.info.domain.models.Statistic

@Composable
internal fun StatisticContents(modifier: Modifier = Modifier, statistics: List<Statistic>) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.wrapContentSize(),
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(statistics) { statistic ->
            StatisticItemContent(statistic = statistic)
        }
        item(
            span = StaggeredGridItemSpan.FullLine,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StatisticItemContent(modifier: Modifier = Modifier, statistic: Statistic) {
    OnBackgroundCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statistic.value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontFamily = FontNunito.bold(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HtmlText(
                textAlign = TextAlign.Center,
                text = statistic.title,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 20.sp
            )
        }
    }
}