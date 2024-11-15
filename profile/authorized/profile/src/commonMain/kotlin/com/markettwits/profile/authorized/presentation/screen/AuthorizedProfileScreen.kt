package com.markettwits.profile.authorized.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_screen.CollapsingToolbarRefreshScaffold
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.authorized.presentation.components.members.MyMembersCard
import com.markettwits.profile.authorized.presentation.components.orders.MyOrdersCard
import com.markettwits.profile.authorized.presentation.components.statistics.UserStartStatistic
import com.markettwits.profile.authorized.presentation.components.statistics.userStatisticMapper
import com.markettwits.profile.authorized.presentation.components.top_bar.ProfileTopBar
import com.markettwits.profile.authorized.presentation.components.user_info.UserInfoCard
import com.markettwits.profile.authorized.presentation.components.user_info.starts.UserStarts
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore


@Composable
fun AuthorizedProfileScreen(component: AuthorizedProfileComponent) {

    val state by component.state.collectAsState()

    var fullImageState by rememberSaveable { mutableStateOf(false) }

    state.user?.let { user ->
        CollapsingToolbarRefreshScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            isRefreshing = state.isLoading, onRefresh = {
                component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
            },
            toolbar = {
                ProfileTopBar(
                    modifier = Modifier.padding(bottom = 5.dp),
                    goSettings = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.Settings)
                    })
            }, body = {
                LazyColumn(modifier = it.padding(14.dp)) {
                    item {
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
                            },
                            onClickSocialNetwork = {
                                component.obtainEvent(
                                    AuthorizedProfileStore.Intent.OnClickUserSocialNetwork(it)
                                )
                            }
                        )
                    }
                    item{
                        MyOrdersCard(
                            onClick = {
                                component.obtainOutput(AuthorizedProfileComponent.Output.UserOrders)
                            }
                        )
                    }
                    item {
                        UserStarts(
                            starts = user.activity.userRegistry,
                            onClickAll = {
                                component.obtainOutput(AuthorizedProfileComponent.Output.AllRegistries)
                            },
                            onClickStart = { start ->
                                component.obtainOutput(
                                    AuthorizedProfileComponent.Output.StartOrder(start)
                                )
                            }
                        )
                    }
                    item {
                        UserStartStatistic(items = userStatisticMapper(user.activity.userRegistry))
                    }
                    item {
                        MyMembersCard(onClick = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.Members)
                        })
                    }
                }
            }
        )
    }
    if (state.isLoading && state.user == null) {
        LoadingFullScreen()
    }
    if (state.isError) {
        FailedScreen(
            message = state.message,
        ) {
            component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
        }
    }
    if (fullImageState) {
        FullImageScreen(image = state.user?.userInfo?.photo ?: "") {
            fullImageState = false
        }
    }
}