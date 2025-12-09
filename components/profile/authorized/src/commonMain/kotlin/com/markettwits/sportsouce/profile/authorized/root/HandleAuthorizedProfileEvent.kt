package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponent.Config.Start
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput

internal fun handleAuthorizedProfileEvent(
    outPut: AuthorizedProfileComponent.Output,
    navigation: StackNavigation<RootAuthorizedProfileComponent.Config>,
) {
    when (outPut) {
        is AuthorizedProfileComponent.Output.EditProfile -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.EditProfileMenu
        )

        is AuthorizedProfileComponent.Output.MyRegistries -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.MyRegistries
        )

        is AuthorizedProfileComponent.Output.SocialNetwork -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.SocialNetwork
        )

        is AuthorizedProfileComponent.Output.Start -> navigation.pushNew(
            Start(StartScreenInput.Id(outPut.startId))
        )

        is AuthorizedProfileComponent.Output.AllRegistries -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.MyRegistries
        )

        is AuthorizedProfileComponent.Output.Members -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.Members
        )

        is AuthorizedProfileComponent.Output.Settings -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.Settings
        )

        is AuthorizedProfileComponent.Output.UserOrders -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.ShopUserOrders
        )

        is AuthorizedProfileComponent.Output.Club -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.Club
        )

        is AuthorizedProfileComponent.Output.StartOrder -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.MyRegistries
        )

        is AuthorizedProfileComponent.Output.Favorites -> navigation.pushNew(
            RootAuthorizedProfileComponent.Config.Favorites
        )
    }
}