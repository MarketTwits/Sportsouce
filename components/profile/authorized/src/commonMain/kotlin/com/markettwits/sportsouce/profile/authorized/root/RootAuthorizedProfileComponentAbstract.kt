package com.markettwits.sportsouce.profile.authorized.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.club.root.RootClubComponentBase
import com.markettwits.sportsouce.edit_profile.root.RootEditProfileComponentBase
import com.markettwits.sportsouce.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponentBase
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponent.Child.*
import com.markettwits.sportsouce.profile.members.member_root.component.RootMembersComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsComponentBase
import com.markettwits.sportsouce.settings.root.RootSettingsComponentBase
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.starts.favorites.di.createStartsFavoritesComponent

abstract class RootAuthorizedProfileComponentAbstract(
    componentContext: ComponentContext,
    private val signOut: () -> Unit,
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
        is RootAuthorizedProfileComponent.Config.AuthProfile -> AuthProfile(
            AuthorizedProfileComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                event = { handleAuthorizedProfileEvent(it, navigation) }
            )
        )

        is RootAuthorizedProfileComponent.Config.MyRegistries -> MyRegistries(
            component = RootRegistrationsComponentBase(
                context = componentContext,
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.EditProfileMenu -> EditProfileMenu(
            RootEditProfileComponentBase(
                componentContext = componentContext,
                pop = navigation::pop,
                signOut = { signOut() },
                openSocialNetworkOnStart = config.openSocialNetworkOnStart
            )
        )

        is RootAuthorizedProfileComponent.Config.SocialNetwork -> SocialNetwork(
            EditProfileSocialNetworkComponentBase(
                componentContext = componentContext,
                storeFactory = scope.get(),
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.Start -> Start(
            RootStartScreenComponentBase(
                context = componentContext,
                input = config.startInput,
                pop = navigation::pop
            )
        )


        is RootAuthorizedProfileComponent.Config.Members -> Members(
            RootMembersComponentBase(
                componentContext = componentContext,
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.Settings -> Settings(
            RootSettingsComponentBase(
                componentContext = componentContext,
                pop = navigation::pop,
            )
        )

        is RootAuthorizedProfileComponent.Config.ShopUserOrders -> ShopUserOrders(
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

        is RootAuthorizedProfileComponent.Config.Club -> ClubDashboard(
            RootClubComponentBase(
                componentContext = componentContext,
                pop = navigation::pop
            )
        )

        is RootAuthorizedProfileComponent.Config.Favorites -> StartsFavorites(
            scope.createStartsFavoritesComponent(
                componentContext = componentContext,
                output = {
                    navigation.pushNew(RootAuthorizedProfileComponent.Config.Start(StartScreenInput.Item(it)))
                },
                pop = navigation::pop
            )
        )
    }
}
