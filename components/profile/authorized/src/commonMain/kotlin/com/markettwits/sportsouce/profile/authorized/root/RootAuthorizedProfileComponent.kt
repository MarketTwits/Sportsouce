package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.club.root.RootClubComponent
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.sportsouce.edit_profile.root.RootEditProfileComponent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.sportsouce.profile.members.member_root.component.RootMembersComponentBase
import com.markettwits.sportsouce.profile.registrations.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.list.presentation.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.root.RootRegistrationsComponent
import com.markettwits.sportsouce.settings.root.RootSettingsComponent
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
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

        @Serializable
        data object ShopUserOrders : Config()

        @Serializable
        data object Club : Config()
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

        data class ShopUserOrders(val component : ShopUserOrdersComponent) : Child()

        data class ClubDashboard(val component : RootClubComponent) : Child()
    }

    @Serializable
    sealed interface SlotChild {
        data class StartOrder(val component: StartOrderComponent) : SlotChild
    }
}