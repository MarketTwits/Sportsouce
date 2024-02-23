package com.markettwits.profile.presentation.deprecated

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
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.change_password.presentation.screen.ChangePasswordComponent
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileComponent
import com.markettwits.edit_profile.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import com.markettwits.edit_profile.root.RootEditProfileComponent
import com.markettwits.edit_profile.root.RootEditProfileComponentBase
import com.markettwits.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponent
import com.markettwits.edit_profile.social_network.presentation.component.EditProfileSocialNetworkComponentBase
import com.markettwits.profile.di.rootProfileModule
import com.markettwits.profile.presentation.component.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileComponentBase
import com.markettwits.profile.presentation.component.my_members.MyMembersComponent
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfile
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfileComponent
import com.markettwits.profile.presentation.sign_in.SignInInstanceKeeper
import com.markettwits.profile.presentation.sign_in.SignInScreenComponent
import com.markettwits.profile.presentation.sign_up.presentation.SignUpComponent
import com.markettwits.profile.presentation.sign_up.presentation.SignUpComponentBase
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.root_registrations.RootRegistrationsComponent
import com.markettwits.registrations.root_registrations.RootRegistrationsComponentBase
import com.markettwits.registrations.start_order_profile.component.StartOrderComponent
import com.markettwits.registrations.start_order_profile.component.StartOrderComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase
import kotlinx.serialization.Serializable

class DefaultProfileComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootProfileModule)
    )
    private val navigation = StackNavigation<Config>()
    private val slotNavigation = SlotNavigation<SlotConfig>()
    private val _configStack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.UnAuthProfile,
            handleBackButton = true,
            childFactory = ::child,
        )

    val childSlot: Value<ChildSlot<*, SlotChild>> = childSlot(
        source = slotNavigation,
        serializer = SlotConfig.serializer(),
        handleBackButton = true,
        childFactory = ::childSlot
    )

    val childStack: Value<ChildStack<*, Child>> get() = _configStack

    private fun childSlot(
        config: SlotConfig,
        componentContext: ComponentContext,
    ): SlotChild =
        when (config) {
            is SlotConfig.StartOrder -> SlotChild.StartOrder(
                StartOrderComponentBase(
                    componentContext = componentContext,
                    start = config.startOrderInfo,
                    storeFactory = scope.get(),
                    dismiss = slotNavigation::dismiss
                )
            )
        }


    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): Child =
        when (config) {
            is Config.Login -> Child.Login(
                SignInScreenComponent(
                    context = componentContext,
                    signInInstanceKeeper = SignInInstanceKeeper(
                        service = scope.get(),
                        toProfile = { navigation.replaceAll(Config.AuthProfile) },
                        toSignUp = { navigation.push(Config.SignUp) }
                    ),
                )
            )

            is Config.AuthProfile -> Child.AuthProfile(
                AuthorizedProfileComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    event = ::handleAuthorizedProfileEvent
                )
            )

            is Config.UnAuthProfile -> Child.UnAuthProfile(
                UnAuthorizedProfileComponent(
                    componentContext,
                    service = scope.get(),
                    goSignIn = {
                        navigation.push(Config.Login)
                    },
                    goAuthProfile = {
                        navigation.replaceCurrent(Config.AuthProfile)
                    }
                )
            )

            is Config.EditProfile -> Child.EditProfile(
                EditProfileComponentBase(
                    componentContext,
                    userId = config.userId,
                    mapper = RemoteUserToEditProfileMapper.Base(),
                    goBack = ::onBackClicked,
                    service = scope.get()
                )
            )

            is Config.MyMembers -> Child.MyMembers(
                MyMembersComponent(componentContext)
            )

            is Config.ChangePassword -> Child.ChangePassword(
                ChangePasswordComponent(
                    context = componentContext,
                    pop = navigation::pop,
                    storeFactory = scope.get()
                )
            )

            is Config.MyRegistries -> Child.MyRegistries(
                RootRegistrationsComponentBase(componentContext, pop = ::onBackClicked)
            )

            is Config.SignUp -> Child.SignUp(
                SignUpComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    pop = ::onBackClicked,
                    profile = { navigation.replaceAll(Config.AuthProfile) }
                )
            )

            is Config.EditProfileMenu -> Child.EditProfileMenu(
                RootEditProfileComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop,
                )
            )

            is Config.SocialNetwork -> Child.SocialNetwork(
                EditProfileSocialNetworkComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = navigation::pop
                )
            )

            is Config.Start -> Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )
        }


    private fun onBackClicked() {
        navigation.pop()
    }

    private fun handleAuthorizedProfileEvent(outPut: AuthorizedProfileComponent.Output) {
        when (outPut) {
            is AuthorizedProfileComponent.Output.EditProfile -> navigation.push(Config.EditProfileMenu)
            is AuthorizedProfileComponent.Output.MyMembers -> navigation.push(Config.MyMembers)
            is AuthorizedProfileComponent.Output.MyRegistries -> navigation.push(Config.MyRegistries)
            is AuthorizedProfileComponent.Output.SocialNetwork -> navigation.push(Config.SocialNetwork)
            is AuthorizedProfileComponent.Output.Start -> navigation.push(Config.Start(outPut.startId))
            is AuthorizedProfileComponent.Output.StartOrder -> slotNavigation.activate(
                SlotConfig.StartOrder(
                    outPut.startOrderInfo
                )
            )
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data class Start(val startId: Int) : Config()

        @Serializable
        data object SocialNetwork : Config()
        @Serializable
        data object EditProfileMenu : Config()

        @Serializable
        data class EditProfile(val userId: Int) : Config()

        @Serializable
        data object AuthProfile : Config()

        @Serializable
        data object UnAuthProfile : Config()

        @Serializable
        data object Login : Config()

        @Serializable
        data object ChangePassword : Config()

        @Serializable
        data object MyMembers : Config()

        @Serializable
        data object MyRegistries : Config()

        @Serializable
        data object SignUp : Config()

    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data class StartOrder(val startOrderInfo: StartOrderInfo) : SlotConfig
    }

    sealed class Child {
        data class Start(val component: RootStartScreenComponentBase) : Child()
        data class SocialNetwork(val component: EditProfileSocialNetworkComponent) : Child()
        data class Login(val component: SignInScreenComponent) : Child()
        data class AuthProfile(val component: AuthorizedProfileComponent) :
            Child()
        data class UnAuthProfile(val component: UnAuthorizedProfile) : Child()
        data class EditProfile(val component: EditProfileComponent) : Child()

        data class ChangePassword(val component: com.markettwits.change_password.presentation.screen.ChangePassword) :
            Child()

        data class MyMembers(val component: com.markettwits.profile.presentation.component.my_members.MyMembers) :
            Child()

        data class MyRegistries(val component: RootRegistrationsComponent) :
            Child()

        data class SignUp(val component: SignUpComponent) : Child()
        data class EditProfileMenu(val component: RootEditProfileComponent) : Child()
    }

    @Serializable
    sealed interface SlotChild {
        data class StartOrder(val component: StartOrderComponent) : SlotChild
    }
}