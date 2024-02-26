package com.markettwits.members.members_list.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.members.members_list.presentation.component.MembersListComponent
import com.markettwits.members.members_list.presentation.components.components.MembersList
import com.markettwits.members.members_list.presentation.store.store.MembersListStore

@Composable
fun MembersScreen(component: MembersListComponent) {
    val state by component.state.collectAsState()
    Scaffold(
        topBar = {
            TopBarWithClip(title = "Мои участники") {
                component.obtainEvent(MembersListStore.Intent.GoBack)
            }
        }
    ) { paddingValues ->
        PullToRefreshScreen(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding()),
            isRefreshing = state.isLoading, onRefresh = {
                component.obtainEvent(MembersListStore.Intent.Retry)
            }) {
            if (state.isSuccess) {
                Column(
                    modifier = it
                        .padding(10.dp)
                ) {
                    MembersList(items = state.members,
                        onClick = {
                            component.obtainEvent(MembersListStore.Intent.OnClickMember(it))
                        },
                        onClickAddMember = {
                            component.obtainEvent(MembersListStore.Intent.OnClickAddMember)
                        }
                    )
                }
            }
            if (state.isError) {
                FailedScreen(onClickHelp = { }) {
                    component.obtainEvent(MembersListStore.Intent.Retry)
                }
            }

        }
    }
}

@Preview
@Composable
private fun MembersScreenPreview() {
    SportSouceTheme {
        //   MembersScreen()
    }
}