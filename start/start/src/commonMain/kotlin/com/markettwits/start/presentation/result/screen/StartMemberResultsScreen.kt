package com.markettwits.start.presentation.result.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.start.presentation.result.components.StartMembersResultTable
import com.markettwits.start.presentation.result.components.StartSearchMemberResults
import com.markettwits.start.presentation.result.store.StartMemberResultsStore

@Composable
fun StartMemberResultsScreen(component: StartMemberResultsComponent) {

    val state by component.state.collectAsState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            StartSearchMemberResults(
                query = state.textQuery,
                onValueChange = {
                    component.obtainEvent(StartMemberResultsStore.Intent.OnChangeQuery(it))
                },
                onClickGoBack = {
                    component.obtainEvent(StartMemberResultsStore.Intent.OnClickGoBack)
                },
                onClickBrush = {
                    component.obtainEvent(StartMemberResultsStore.Intent.OnClickBrush)
                }
            )
        }
    ) {
        StartMembersResultTable(items = state.visibleMembersResult)
    }
}