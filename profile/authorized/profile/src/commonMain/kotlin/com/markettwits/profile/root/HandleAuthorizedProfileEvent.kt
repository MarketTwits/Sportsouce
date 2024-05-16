package com.markettwits.profile.root

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponent

internal fun handleAuthorizedProfileEvent(
    outPut: AuthorizedProfileComponent.Output,
    navigation: StackNavigation<RootAuthorizedProfileComponent.Config>,
    slotNavigation: SlotNavigation<RootAuthorizedProfileComponent.SlotConfig>
) {
    when (outPut) {
        is AuthorizedProfileComponent.Output.EditProfile -> navigation.push(
            RootAuthorizedProfileComponent.Config.EditProfileMenu
        )

        is AuthorizedProfileComponent.Output.MyRegistries -> navigation.push(
            RootAuthorizedProfileComponent.Config.MyRegistries
        )

        is AuthorizedProfileComponent.Output.SocialNetwork -> navigation.push(
            RootAuthorizedProfileComponent.Config.SocialNetwork
        )

        is AuthorizedProfileComponent.Output.Start -> navigation.push(
            RootAuthorizedProfileComponent.Config.Start(outPut.startId)
        )

        is AuthorizedProfileComponent.Output.StartOrder -> slotNavigation.activate(
            RootAuthorizedProfileComponent.SlotConfig.StartOrder(
                outPut.startOrderInfo
            )
        )

        is AuthorizedProfileComponent.Output.AllRegistries -> navigation.push(
            RootAuthorizedProfileComponent.Config.UserStarts
        )

        is AuthorizedProfileComponent.Output.Members -> navigation.push(
            RootAuthorizedProfileComponent.Config.Members
        )

        is AuthorizedProfileComponent.Output.Settings -> navigation.push(
            RootAuthorizedProfileComponent.Config.Settings
        )
    }
}