package com.markettwits.sportsouce.profile.authorized.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.club.root.RootClubScreen
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.screen.ProfileSocialNetworkScreen
import com.markettwits.sportsouce.edit_profile.root.RootEditProfileScreen
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.screen.AuthorizedProfileScreen
import com.markettwits.sportsouce.profile.members.member_root.component.RootMembersScreen
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsScreen
import com.markettwits.sportsouce.settings.root.RootSettingsScreen
import com.markettwits.sportsouce.shop.orders.presentation.screen.ShopUserOrdersScreen
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.starts.favorites.presentation.screen.FavoriteStartsScreen

@Composable
fun RootAuthorizedProfileScreen(component: RootAuthorizedProfileComponent) {

    val childStack by component.childStack.subscribeAsState()

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            
            is RootAuthorizedProfileComponent.Child.AuthProfile -> AuthorizedProfileScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.MyRegistries -> RootRegistrationsScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.EditProfileMenu -> RootEditProfileScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.SocialNetwork -> ProfileSocialNetworkScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.Start -> RootStartScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.Members -> RootMembersScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.Settings -> RootSettingsScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.ShopUserOrders -> ShopUserOrdersScreen(
                component = child.component
            )

            is RootAuthorizedProfileComponent.Child.ClubDashboard -> RootClubScreen(component = child.component)

            is RootAuthorizedProfileComponent.Child.StartsFavorites -> FavoriteStartsScreen(
                component = child.component
            )
        }
    }
}