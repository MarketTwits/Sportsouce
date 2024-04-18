package com.markettwits.starts.starts.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TabBar(
    content: @Composable ((page: Int) -> Unit),
) {
    val scope = rememberCoroutineScope()
    val pages = listOf("Главная", "Ближайшие", "Прошедшие", "Анонсы")
    val pagerState = rememberPagerState(pageCount = pages::size, initialPage = 1)
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabRowDefaults.PrimaryIndicator(
            width = 120.dp,
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                .fillMaxWidth()
        )
    }
    ScrollableTabRow(
        modifier = Modifier.height(50.dp),
        selectedTabIndex = pagerState.currentPage,
        divider = {},
        indicator = indicator,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        pages.forEachIndexed { index, item ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp)),
                selectedContentColor = MaterialTheme.colorScheme.tertiary,
                unselectedContentColor = Color.Gray,
                selected = selected,
                text = {
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.bold()
                    )
                },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = index,
                            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                        )
                    }
                })
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        Column {
            content(page)
        }
    }
}
