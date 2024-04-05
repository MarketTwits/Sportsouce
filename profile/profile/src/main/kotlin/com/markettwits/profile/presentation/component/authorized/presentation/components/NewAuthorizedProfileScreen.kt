package com.markettwits.profile.presentation.component.authorized.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.FullImageScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.profile.presentation.component.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.presentation.components.members.MyMembersCard
import com.markettwits.profile.presentation.component.authorized.presentation.components.statistics.UserStartStatistic
import com.markettwits.profile.presentation.component.authorized.presentation.components.statistics.userStatisticMapper
import com.markettwits.profile.presentation.component.authorized.presentation.components.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.UserInfoCard
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.starts.UserStarts
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore


@Composable
fun NewAuthorizedProfileScreen(component: AuthorizedProfileComponent) {
    val state by component.state.collectAsState()
    var fullImageState by rememberSaveable { mutableStateOf(false) }
    state.user?.let { user ->
        PullToRefreshScreen(isRefreshing = state.isLoading, onRefresh = {
            component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
        }) { modifier ->
            Scaffold(
                topBar = {
                    ProfileTopBar(goSettings = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                    })
                }) {
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = it.calculateTopPadding())
                        .padding(14.dp)
                ) {
                    UserInfoCard(
                        user = user,
                        onClickAddSocialNetwork = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.SocialNetwork)
                        },
                        onClickEdit = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                        },
                        onClickImage = {
                            fullImageState = true
                        }
                    )
                    UserStarts(
                        starts = user.activity.userRegistry,
                        onClickAll = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.AllRegistries)
                        },
                        onClickStart = { start ->
                            component.obtainOutput(
                                AuthorizedProfileComponent.Output.StartOrder(
                                    start
                                )
                            )
                        }
                    )
                    UserStartStatistic(items = userStatisticMapper(user.activity.userRegistry))
                    MyMembersCard(onClick = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.Members)
                    })
                }
            }
        }
    }
    if (state.isLoading && state.user == null) {
        LoadingFullScreen()
    }
    if (state.isError) {
        FailedScreen(
            message = state.message,
            onClickHelp = { }) {
            component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
        }
    }
    if (fullImageState) {
        FullImageScreen(image = state.user?.userInfo?.photo ?: "") {
            fullImageState = false
        }
    }
}