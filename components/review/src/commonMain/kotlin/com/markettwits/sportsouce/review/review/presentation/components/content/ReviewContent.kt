package com.markettwits.sportsouce.review.review.presentation.components.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.news.common.model.NewsItem
import com.markettwits.news.news_list.components.NewsContent
import com.markettwits.sportsouce.review.review.presentation.components.actual.ActualStarts
import com.markettwits.sportsouce.review.review.presentation.components.archive.ArchiveStarts
import com.markettwits.sportsouce.review.review.presentation.components.review_menu.ReviewMenu
import com.markettwits.sportsouce.review.review.presentation.components.social_network.SocialNetwork
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun ReviewContent(
    news: List<NewsItem>,
    actual: List<StartsListItem>,
    archive: List<StartsListItem>,
    onClickStart: (Int) -> Unit,
    onClickNewsInfo: (NewsItem) -> Unit,
    onClickMenu: (Int) -> Unit,
    onClickTelegram: () -> Unit,
    onClickVk: () -> Unit,
    notification: @Composable ((Modifier) -> Unit),
) {
    if (news.isNotEmpty()) {
        NewsContent(items = news) {
            onClickNewsInfo(it)
        }
    }
    ReviewMenu {
        onClickMenu(it)
    }
    notification(Modifier)
    if (actual.isNotEmpty()) {
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        ActualStarts(starts = actual) {
            onClickStart(it)
        }
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        ArchiveStarts(starts = archive) {
            onClickStart(it)
        }
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        SocialNetwork(onClickVk = onClickVk, onClickTelegram = onClickTelegram)
    }
}