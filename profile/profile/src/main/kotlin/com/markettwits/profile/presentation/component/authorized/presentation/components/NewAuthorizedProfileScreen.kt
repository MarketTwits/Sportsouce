package com.markettwits.profile.presentation.component.authorized.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.profile.presentation.component.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.presentation.components.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.UserInfoCard
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.starts.UserStarts
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore


@Composable
fun NewAuthorizedProfileScreen(component: AuthorizedProfileComponent) {
    val state by component.state.collectAsState()
    state.user?.let { user ->
        PullToRefreshScreen(isRefreshing = state.isLoading, onRefresh = {
            component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
        }) { modifier ->
            Scaffold(
                containerColor = MaterialTheme.colorScheme.primary,
                topBar = {
                    ProfileTopBar(goSettings = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                    })
                }) {
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = it.calculateTopPadding())
                        .padding(10.dp)
                ) {
                    UserInfoCard(
                        user = user,
                        onClickAddSocialNetwork = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.SocialNetwork)
                        },
                        onClickEdit = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                        })
//                    StartOrderCardColumnList(starts = user.activity.userRegistry){
//                        component.obtainOutput(AuthorizedProfileComponent.Output.StartOrder(it))
//                    }
                    UserStarts(
                        starts = user.activity.userRegistry,
                        onClick = {
                            //  component.obtainOutput(AuthorizedProfileComponent.Output.Start(it))
                        },
                        onClickStart = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.StartOrder(it))
                        }
                    )
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
}