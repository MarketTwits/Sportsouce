package com.markettwits.sportsouce.profile.authorized.authorized.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable.ProfileScreenContent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable.ProfileTopBar
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore


@Composable
fun AuthorizedProfileScreen(component: AuthorizedProfileComponent) {

    val state by component.state.collectAsState()

    var fullImageState by rememberSaveable { mutableStateOf(false) }

    state.user?.let { user ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                ProfileTopBar(
                    modifier = Modifier.padding(bottom = 5.dp),
                    goSettings = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.Settings)
                    })
            }, content = { paddingValues ->
                PullToRefreshScreen(
                    isRefreshing = false,
                    onRefresh = {
                        component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
                    }
                ) {
                    ProfileScreenContent(
                        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                        userName = user.userInfo.name,
                        userRegistrationsCount = user.activity.userRegistry.size,
                        userImageUrl = user.userInfo.photo,
                        onClickStarts = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.AllRegistries)
                        },
                        onClickMembers = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.Members)
                        },
                        onClickOrders = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.UserOrders)
                        },
                        onClickEditProfile = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                        },
                        onClickFavorites = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.Favorites)
                        },
                        onClickClub = {
                            component.obtainOutput(AuthorizedProfileComponent.Output.Club)
                        },
                    )
                }
                if (state.isLoading && state.user == null) {
                    LoadingFullScreen()
                }
                if (state.error != null) {
                    state.error!!.SauceErrorScreen(onClickRetry = {
                        component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
                    })
                }
                if (fullImageState) {
                    FullImageScreen(image = state.user?.userInfo?.photo ?: "") {
                        fullImageState = false
                    }
                }
            })
    }
}