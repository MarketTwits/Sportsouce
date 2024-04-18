package com.markettwits.start.presentation.membres.list.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.start.presentation.membres.list.compoents.StartMembers
import com.markettwits.start.presentation.membres.list.compoents.StartSearchMember
import com.markettwits.start.presentation.membres.list.component.StartMembersScreen

@Composable
fun StartMembersScreen(component: StartMembersScreen) {
    val members by component.members.subscribeAsState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            Column {
                TopBarBase(title = "Список участников") {
                    component.back()
                }
                StartSearchMember(component = component)
            }
        }
    ) {
        StartMembers(members = members)
    }
}