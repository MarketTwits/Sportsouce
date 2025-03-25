package com.markettwits.sportsouce.start.presentation.membres.list.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.sportsouce.start.presentation.membres.list.compoents.StartMembersTable
import com.markettwits.sportsouce.start.presentation.membres.list.compoents.StartSearchMember
import com.markettwits.sportsouce.start.presentation.membres.list.component.StartMembersScreen

@Composable
fun StartMembersScreen(component: StartMembersScreen) {
    val members by component.members.subscribeAsState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            StartSearchMember(
                component = component
            )
        }
    ) {
        StartMembersTable(items = members)
    }
}