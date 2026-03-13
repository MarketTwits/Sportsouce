package com.markettwits.sportsouce.profile.authorized.authorized.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable.ProfileScreenContent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput


@Composable
fun AuthorizedProfileScreen(component: AuthorizedProfileComponent) {

    val state by component.state.collectAsState()

    var fullImageState by rememberSaveable { mutableStateOf(false) }
    val intentAction = rememberIntentActionByPlatform()

    state.user?.let { user ->
        ProfileScreenContent(
            isRefreshing = state.isLoading,
            userName = user.userInfo.name,
            userPhoneNumber = user.userInfo.phoneNumber,
            userRegistrationsCount = user.activity.userRegistry.size,
            userRegistrations = user.activity.userRegistry,
            recentStarts = state.recentStarts,
            userImageUrl = user.userInfo.photo,
            socialNetwork = user.socialNetwork,
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
            oClickSettings = {
                component.obtainOutput(AuthorizedProfileComponent.Output.Settings)
            },
            onRefresh = {
                component.obtainEvent(AuthorizedProfileStore.Intent.Retry)
            },
            onClickRegistration = { registration ->
                component.obtainOutput(
                    AuthorizedProfileComponent.Output.Start(
                        StartScreenInput.Id(registration.startId)
                    )
                )
            },
            onClickRecentStart = { start ->
                component.obtainOutput(
                    AuthorizedProfileComponent.Output.Start(
                        StartScreenInput.Item(start)
                    )
                )
            },
            onSocialNetworkClick = { url ->
                intentAction.openWebPage(url)
            },
            onAddSocialNetwork = {
                component.obtainOutput(AuthorizedProfileComponent.Output.SocialNetwork)
            }
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
}
