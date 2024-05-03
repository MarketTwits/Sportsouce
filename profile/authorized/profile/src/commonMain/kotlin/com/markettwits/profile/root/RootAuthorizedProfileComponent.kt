package com.markettwits.profile.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.edit_profile.root.RootEditProfileComponent
import com.markettwits.members.member_root.component.RootMembersComponentBase
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.registrations.detail.component.StartOrderComponent
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.list.presentation.component.RegistrationsComponent
import com.markettwits.registrations.root.RootRegistrationsComponent
import com.markettwits.settings.root.RootSettingsComponent
import com.markettwits.start.root.RootStartScreenComponentBase
import kotlinx.serialization.Serializable

interface RootAuthorizedProfileComponent {
    val childStack: Value<ChildStack<*, Child>>
    val childSlot: Value<ChildSlot<*, SlotChild>>

    @Serializable
    sealed class Config {
        @Serializable
        data object Members : Config()

        @Serializable
        data class Start(val startId: Int) : Config()

        @Serializable
        data object UserStarts : Config()

        @Serializable
        data object SocialNetwork : Config()

        @Serializable
        data object EditProfileMenu : Config()

        @Serializable
        data object AuthProfile : Config()

        @Serializable
        data object MyRegistries : Config()

        @Serializable
        data object Settings : Config()
    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data class StartOrder(val startOrderInfo: StartOrderInfo) : SlotConfig
    }

    sealed class Child {
        data class Members(val component: RootMembersComponentBase) : Child()
        data class UserStarts(val component: RegistrationsComponent) : Child()
        data class Start(val component: RootStartScreenComponentBase) : Child()
        data class SocialNetwork(val component: EditProfileSocialNetworkComponent) : Child()
        data class AuthProfile(val component: AuthorizedProfileComponent) : Child()
        data class MyRegistries(val component: RootRegistrationsComponent) : Child()
        data class EditProfileMenu(val component: RootEditProfileComponent) : Child()
        data class Settings(val component: RootSettingsComponent) : Child()
    }

    @Serializable
    sealed interface SlotChild {
        data class StartOrder(val component: StartOrderComponent) : SlotChild
    }
}