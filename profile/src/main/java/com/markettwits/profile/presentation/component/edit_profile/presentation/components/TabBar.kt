package com.markettwits.profile.presentation.component.edit_profile.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.markettwits.core_ui.theme.SportSouceColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabBar(modifier : Modifier = Modifier, content: @Composable (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    val pages = listOf("Мои соцсети","Данные", "О мне")
    val pagerState = rememberPagerState(pageCount = pages::size, initialPage = 1)

    val indicator = @Composable { tabPositions: List<TabPosition> ->

        TabRowDefaults.PrimaryIndicator(
            width = 120.dp,
            shape = RoundedCornerShape(10.dp),
            color = SportSouceColor.SportSouceBlue,
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                .fillMaxWidth()
        )
    }
    ScrollableTabRow(
        modifier = Modifier.height(50.dp).background(Color.White),
        selectedTabIndex = pagerState.currentPage,
        divider = {},
        indicator = indicator
    ) {
        pages.forEachIndexed { index, item ->
            Tab(
                modifier = Modifier
                    .background(Color.White)
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp)),
                selectedContentColor = SportSouceColor.SportSouceBlue,
                unselectedContentColor = Color.Gray,
                selected = pagerState.currentPage == index,
                text = {
                    Text(
                        text = item,
                        fontSize = 16.sp,
                    )
                },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        content(page)
    }
}