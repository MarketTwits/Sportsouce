package com.markettwits.sportsouce.starts.starts.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.SauceTopBarSection
import com.markettwits.core_ui.items.components.selection.SauceScrollableTabs
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TabBar(
    onPageChanged: (Int) -> Unit,
    content: @Composable ((page: Int) -> Unit),
) {
    val scope = rememberCoroutineScope()
    val pages = listOf("Главная", "Ближайшие", "Прошедшие", "Анонсы")
    val pagerState = rememberPagerState(pageCount = pages::size, initialPage = 1)
    SauceTopBarSection {
        SauceScrollableTabs(
            labels = pages,
            selectedIndex = pagerState.currentPage,
            onSelected = { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                }
            },
        )
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        Column {
            content(page)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }
}
