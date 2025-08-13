package com.markettwits.sportsouce.start.presentation.membres.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.sportsouce.start.presentation.membres.components.StartSearchMember
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreen
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreenComponent
import com.markettwits.sportsouce.start.presentation.membres.components.StartMembersItemsContent
import com.markettwits.sportsouce.start.presentation.membres.components.StartMembersFilterDialog

@Composable
fun StartMembersScreen(component: StartMembersScreen) {
    val state by component.state.collectAsState()
    val pagingItems = state.membersItems.collectAsLazyPagingItems()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            StartSearchMember(
                modifier = Modifier,
                component = component
            )
        }
    ) {
        StartMembersItemsContent(
            modifier = Modifier,
            totalCount = state.totalCount,
            items = pagingItems
        )
        // Filter Dialog
        if (component is StartMembersScreenComponent) {
            StartMembersFilterDialog(component)
        }
    }
}