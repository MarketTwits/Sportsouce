package com.markettwits.sportsouce.start.presentation.result.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.sportsouce.start.presentation.result.components.MemberResultDetailed
import com.markettwits.sportsouce.start.presentation.result.components.StartMemberMemberResultsTopbar
import com.markettwits.sportsouce.start.presentation.result.components.StartMemberResultsItemsContentPaging
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun StartMemberResultsScreen(component: StartMemberResultsComponent) {

    val state by component.state.collectAsState()
    val pagingItems = state.membersItems.collectAsLazyPagingItems()

    var currentMemberResult by remember { mutableStateOf<MemberResult?>(null) }
    val toolbarState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = toolbarState,
        toolbar = {
            StartMemberMemberResultsTopbar(
                state = state,
                windowSizeClass = calculateWindowSizeClass(),
                onIntent = component::obtainEvent,
                component = component
            )
        }
    ) {
        StartMemberResultsItemsContentPaging(
            totalCount = state.totalCount,
            items = pagingItems,
            onClickMemberResult = { member ->
                currentMemberResult = member
            }
        )
        if (currentMemberResult != null) {
            DefaultModalBottomSheet(
                onDismissRequest = {
                    currentMemberResult = null
                },
                dragHandle = {}
            ) {
                MemberResultDetailed(
                    memberResult = currentMemberResult!!,
                    onBackClick = {
                        currentMemberResult = null
                    }
                )
            }
        }
    }
}