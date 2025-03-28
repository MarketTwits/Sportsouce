package com.markettwits.sportsouce.profile.members.members_list.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.profile.members.members_list.presentation.component.MembersListComponent
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.MembersList
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore

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
            }) { modifier ->
            AdaptivePane {
                if (state.isSuccess) {
                    Column(
                        modifier = modifier
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
                    FailedScreen(
                        message = state.message,
                        onClickRetry = {
                            component.obtainEvent(MembersListStore.Intent.Retry)
                        },
                        onClickBack = {
                            component.obtainEvent(MembersListStore.Intent.GoBack)
                        }
                    )
                }
            }

        }
    }
}