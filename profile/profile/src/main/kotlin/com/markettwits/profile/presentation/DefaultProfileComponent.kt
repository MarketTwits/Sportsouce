package com.markettwits.profile.presentation

import com.arkivanov.decompose.ComponentContext
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
import com.markettwits.profile.di.rootProfileModule
import com.markettwits.profile.presentation.component.authorized.profile.AuthorizedProfile
import com.markettwits.profile.presentation.component.authorized.profile.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.profile.AuthorizedProfileEvent
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileComponentBase
import com.markettwits.profile.presentation.component.my_members.MyMembersComponent
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfile
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfileComponent
import com.markettwits.profile.presentation.sign_in.SignInInstanceKeeper
import com.markettwits.profile.presentation.sign_in.SignInScreenComponent
import com.markettwits.profile.presentation.sign_up.presentation.SignUpComponent
import com.markettwits.profile.presentation.sign_up.presentation.SignUpComponentBase
import com.markettwits.registrations.root_registrations.RootRegistrationsComponent
import com.markettwits.registrations.root_registrations.RootRegistrationsComponentBase
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
    private val _configStack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.UnAuthProfile,
            handleBackButton = true,
            childFactory = ::child,
        )

    val childStack: Value<ChildStack<*, Child>> get() = _configStack

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
                AuthorizedProfileComponent(
                    context = componentContext,
                    service = scope.get(),
                    event = {
                        handleAuthorizedProfileEvent(it)
                    }
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

            Config.EditProfileMenu -> Child.EditProfileMenu(
                RootEditProfileComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )
        }


    private fun onBackClicked() {
        navigation.pop()
    }

    private fun handleAuthorizedProfileEvent(event: AuthorizedProfileEvent) {
        when (event) {
            is AuthorizedProfileEvent.SignOut -> navigation.replaceAll(Config.UnAuthProfile)
            is AuthorizedProfileEvent.EditProfile -> navigation.push(Config.EditProfileMenu)
            is AuthorizedProfileEvent.MyRegistries -> navigation.push(Config.MyRegistries)
            is AuthorizedProfileEvent.ChangePasswordProfile -> navigation.push(Config.ChangePassword)
            is AuthorizedProfileEvent.MyMembers -> navigation.push(Config.MyMembers)
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object EditProfileMenu : Config()

        @Serializable
        data class EditProfile(val userId: Int) :
            Config()

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

    sealed class Child {
        data class Login(val component: SignInScreenComponent) : Child()
        data class AuthProfile(val component: AuthorizedProfile) : Child()
        data class UnAuthProfile(val component: UnAuthorizedProfile) : Child()
        data class EditProfile(val component: EditProfileComponent) :
            Child()

        data class ChangePassword(val component: com.markettwits.change_password.presentation.screen.ChangePassword) :
            Child()

        data class MyMembers(val component: com.markettwits.profile.presentation.component.my_members.MyMembers) :
            Child()

        data class MyRegistries(val component: RootRegistrationsComponent) :
            Child()

        data class SignUp(val component: SignUpComponent) : Child()
        data class EditProfileMenu(val component: RootEditProfileComponent) : Child()
    }
}