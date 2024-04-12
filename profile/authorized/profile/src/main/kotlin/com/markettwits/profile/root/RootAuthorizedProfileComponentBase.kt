package com.markettwits.profile.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.edit_profile.edit_social_network.presentation.component.EditProfileSocialNetworkComponentBase
import com.markettwits.edit_profile.root.RootEditProfileComponentBase
import com.markettwits.members.member_root.component.RootMembersComponentBase
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.registrations.registrations_list.presentation.component.RegistrationsComponentBase
import com.markettwits.registrations.registrations_list.presentation.store.RegistrationsDataStoreFactory
import com.markettwits.registrations.root_registrations.RootRegistrationsComponentBase
import com.markettwits.registrations.start_order_detail.component.StartOrderComponentBase
import com.markettwits.settings.root.RootSettingsComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase

class RootAuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    private val signOut: () -> Unit
) : ComponentContext by componentContext, RootAuthorizedProfileComponent {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootProfileModule)
    )
    private val navigation = StackNavigation<RootAuthorizedProfileComponent.Config>()

    private val slotNavigation = SlotNavigation<RootAuthorizedProfileComponent.SlotConfig>()

    override val childSlot: Value<ChildSlot<*, RootAuthorizedProfileComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            serializer = RootAuthorizedProfileComponent.SlotConfig.serializer(),
            handleBackButton = true,
            childFactory = ::childSlot
        )


    override val childStack: Value<ChildStack<*, RootAuthorizedProfileComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootAuthorizedProfileComponent.Config.serializer(),
            initialConfiguration = RootAuthorizedProfileComponent.Config.AuthProfile,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun childSlot(
        config: RootAuthorizedProfileComponent.SlotConfig,
        componentContext: ComponentContext,
    ): RootAuthorizedProfileComponent.SlotChild =
        when (config) {
            is RootAuthorizedProfileComponent.SlotConfig.StartOrder -> RootAuthorizedProfileComponent.SlotChild.StartOrder(
                StartOrderComponentBase(
                    componentContext = componentContext,
                    start = config.startOrderInfo,
                    storeFactory = scope.get(),
                    openStart = { navigation.push(RootAuthorizedProfileComponent.Config.Start(it)) },
                    dismiss = slotNavigation::dismiss
                )
            )
        }


    private fun child(
        config: RootAuthorizedProfileComponent.Config,
        componentContext: ComponentContext,
    ): RootAuthorizedProfileComponent.Child =
        when (config) {
            is RootAuthorizedProfileComponent.Config.AuthProfile -> RootAuthorizedProfileComponent.Child.AuthProfile(
                AuthorizedProfileComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    event = ::handleAuthorizedProfileEvent
                )
            )
            is RootAuthorizedProfileComponent.Config.MyRegistries -> RootAuthorizedProfileComponent.Child.MyRegistries(
                RootRegistrationsComponentBase(componentContext, pop = ::onBackClicked)
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
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootAuthorizedProfileComponent.Config.UserStarts -> RootAuthorizedProfileComponent.Child.UserStarts(
                RegistrationsComponentBase(
                    component = componentContext,
                    storeFactory = RegistrationsDataStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        dataSource = scope.get()
                    ),
                    pop = navigation::pop,
                    onItemClick = {
                        slotNavigation.activate(
                            RootAuthorizedProfileComponent.SlotConfig.StartOrder(
                                it
                            )
                        )
                    },
                )
            )

            is RootAuthorizedProfileComponent.Config.Members -> RootAuthorizedProfileComponent.Child.Members(
                RootMembersComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )

            RootAuthorizedProfileComponent.Config.Settings -> RootAuthorizedProfileComponent.Child.Settings(
                RootSettingsComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop,
                )
            )
        }


    private fun onBackClicked() {
        navigation.pop()
    }

    private fun handleAuthorizedProfileEvent(outPut: AuthorizedProfileComponent.Output) {
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

            AuthorizedProfileComponent.Output.Settings -> navigation.push(
                RootAuthorizedProfileComponent.Config.Settings
            )
        }
    }
}