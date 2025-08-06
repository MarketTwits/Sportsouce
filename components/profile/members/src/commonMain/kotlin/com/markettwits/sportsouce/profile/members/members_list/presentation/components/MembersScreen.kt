package com.markettwits.sportsouce.profile.members.members_list.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        },
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) { paddingValues ->
        PullToRefreshScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            isRefreshing = state.isLoading,
            onRefresh = {
                component.obtainEvent(MembersListStore.Intent.Retry)
            }
        ) { innerModifier ->
            AdaptivePane {
                AnimatedContent(
                    targetState = when {
                        state.isSuccess -> "success"
                        state.isError -> "error"
                        else -> "loading"
                    },
                    transitionSpec = {
                        fadeIn(animationSpec = tween(400)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    },
                    label = "screen_transition"
                ) { screenState ->
                    when (screenState) {
                        "success" -> {
                            MembersList(
                                modifier = innerModifier,
                                items = state.members,
                                onClick = {
                                    component.obtainEvent(MembersListStore.Intent.OnClickMember(it))
                                },
                                onClickAddMember = {
                                    component.obtainEvent(MembersListStore.Intent.OnClickAddMember)
                                }
                            )
                        }

                        "error" -> {
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

                        else -> {
                            // Loading state handled by PullToRefreshScreen
                        }
                    }
                }
            }
        }
    }
}
