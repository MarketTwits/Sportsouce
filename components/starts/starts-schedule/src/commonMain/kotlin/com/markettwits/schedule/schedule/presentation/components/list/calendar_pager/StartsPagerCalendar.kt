package com.markettwits.schedule.schedule.presentation.components.list.calendar_pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartsPagerCalendar(
    state: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
    pages: List<PagerTopBar>
) {
    val scope = rememberCoroutineScope()
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabRowDefaults.PrimaryIndicator(
            width = 110.dp,
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[state.currentPage])
                .fillMaxWidth()
        )
    }
    ScrollableTabRow(
        modifier = modifier.height(50.dp),
        selectedTabIndex = state.currentPage,
        divider = {},
        indicator = indicator,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        pages.forEachIndexed { index, item ->
            StartTab(
                selected = state.currentPage == index,
                icon = item.icon,
                title = item.title,
            ) {
                scope.launch {
                    state.animateScrollToPage(page = index)
                }
            }
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
        state = state,
    ) { page ->
        content(page)
    }
}

@Composable
private fun StartTab(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: String,
    icon: ImageVector?,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            },
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.bold()
        )
    }
}
