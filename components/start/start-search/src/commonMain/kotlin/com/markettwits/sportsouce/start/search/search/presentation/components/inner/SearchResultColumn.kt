package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartsScreenContent

@Composable
fun ColumnScope.SearchResultColumn(
    starts: List<StartsListItem>,
    onClickStart: (StartsListItem, String) -> Unit,
) {
    AnimatedContent(
        targetState = starts.isNotEmpty(),
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        },
        label = "search_results"
    ) { hasResults ->
        if (hasResults) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 4.dp),
                    text = "РЕЗУЛЬТАТЫ ПОИСКА",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.bold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Visible
                )
                StartsScreenContent(
                    items = starts,
                    isMaxWith = true
                ) { startId ->
                    val startTitle = starts.find { it.id == startId.id }?.name
                    if (startTitle != null)
                        onClickStart(startId, startTitle)
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                text = "По вашему запросу ничего не найдено",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Visible
            )
        }
    }
}