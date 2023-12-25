package com.markettwits.topbar

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch


@Preview
@Composable
private fun TabBarPreview() {
    TabBar {}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabBar(content: @Composable (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    val pages = listOf("Главная","Ближайшие", "Прошедшие", "Анонсы")
    val pagerState = rememberPagerState(pageCount = pages::size, initialPage = 1)

    val indicator = @Composable { tabPositions: List<TabPosition> ->

        TabRowDefaults.PrimaryIndicator(
            width = 120.dp,
            shape = RoundedCornerShape(10.dp),
            color = Color(46, 63, 110),
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                .fillMaxWidth()
        )
    }
    ScrollableTabRow(
        modifier = Modifier.height(50.dp),
        selectedTabIndex = pagerState.currentPage,
        divider = {},
        indicator = indicator
    ) {
        pages.forEachIndexed { index, item ->
            Tab(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp))
                ,
                selectedContentColor = Color(46, 63, 110),
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
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        content(page)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomIndicator(tabPositions: List<TabPosition>, pagerState: PagerState) {
    val transition = updateTransition(pagerState.currentPage)
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    Box(
        Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.BottomStart)
            .width(indicatorEnd - indicatorStart)
            .padding(2.dp)
            .fillMaxSize()
            .background(color = Color(0xFFFF7455), RoundedCornerShape(50))
            .border(BorderStroke(2.dp, Color(0xFFC13D25)), RoundedCornerShape(50))
            .zIndex(1f)
    )
}