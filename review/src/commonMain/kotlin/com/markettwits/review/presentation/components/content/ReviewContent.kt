package com.markettwits.review.presentation.components.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.news_list.presentation.components.NewsContent
import com.markettwits.review.presentation.components.actual.ActualStarts
import com.markettwits.review.presentation.components.archive.ArchiveStarts
import com.markettwits.review.presentation.components.review_menu.ReviewMenu
import com.markettwits.review.presentation.components.social_network.SocialNetwork
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun ReviewContent(
    news: List<NewsInfo>,
    actual: List<StartsListItem>,
    archive: List<StartsListItem>,
    onClickStart: (Int) -> Unit,
    onClickNewsInfo: (NewsInfo) -> Unit,
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