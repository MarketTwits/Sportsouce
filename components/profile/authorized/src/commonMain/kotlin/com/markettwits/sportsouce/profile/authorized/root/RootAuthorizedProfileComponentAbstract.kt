package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.club.root.RootClubComponentBase
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponentBase
import com.markettwits.sportsouce.edit_profile.root.RootEditProfileComponentBase
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.sportsouce.profile.members.member_root.component.RootMembersComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsComponentBase
import com.markettwits.sportsouce.settings.root.RootSettingsComponentBase
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponentBase
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase

abstract class RootAuthorizedProfileComponentAbstract(
    componentContext: ComponentContext,
    private val signOut: () -> Unit
) : ComponentContext by componentContext, RootAuthorizedProfileComponent {

    private val scope = getOrCreateKoinScope(
        listOf(rootProfileModule)
    )

    private val navigation = StackNavigation<RootAuthorizedProfileComponent.Config>()

    override val childStack: Value<ChildStack<*, RootAuthorizedProfileComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootAuthorizedProfileComponent.Config.serializer(),
            initialConfiguration = RootAuthorizedProfileComponent.Config.AuthProfile,
            handleBackButton = true,
            childFactory = ::childStack,
        )


    private fun childStack(
        config: RootAuthorizedProfileComponent.Config,
        componentContext: ComponentContext,
    ): RootAuthorizedProfileComponent.Child = when (config) {
        is RootAuthorizedProfileComponent.Config.AuthProfile -> RootAuthorizedProfileComponent.Child.AuthProfile(
            AuthorizedProfileComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                event = { handleAuthorizedProfileEvent(it, navigation) }
            )
        )

        is RootAuthorizedProfileComponent.Config.MyRegistries -> RootAuthorizedProfileComponent.Child.MyRegistries(
            component = RootRegistrationsComponentBase(
                context = componentContext,
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.EditProfileMenu -> RootAuthorizedProfileComponent.Child.EditProfileMenu(
            RootEditProfileComponentBase(
                componentContext = componentContext,
                pop = navigation::pop,
                signOut = { signOut() }
            )
        )

        is RootAuthorizedProfileComponent.Config.SocialNetwork -> RootAuthorizedProfileComponent.Child.SocialNetwork(
            EditProfileSocialNetworkComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.Start -> RootAuthorizedProfileComponent.Child.Start(
            RootStartScreenComponentBase(
                context = componentContext,
                input = com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput.Id(config.startId),
                pop = navigation::pop
            )
        )


        is RootAuthorizedProfileComponent.Config.Members -> RootAuthorizedProfileComponent.Child.Members(
            RootMembersComponentBase(
                componentContext = componentContext,
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.Settings -> RootAuthorizedProfileComponent.Child.Settings(
            RootSettingsComponentBase(
                componentContext = componentContext,
                pop = navigation::pop,
            )
        )

        is RootAuthorizedProfileComponent.Config.ShopUserOrders -> RootAuthorizedProfileComponent.Child.ShopUserOrders(
            ShopUserOrdersComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                outputs = object : ShopUserOrdersComponent.Outputs {
                    override fun goBack() {
                        navigation.pop()
                    }
                }
            )
        )

       is  RootAuthorizedProfileComponent.Config.Club -> RootAuthorizedProfileComponent.Child.ClubDashboard(
           RootClubComponentBase(
               componentContext = componentContext,
               pop = navigation::pop
           )
       )
    }
}