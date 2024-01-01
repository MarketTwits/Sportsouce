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
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfile
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfileEvent
import com.markettwits.profile.presentation.component.change_password.ChangePasswordComponent
import com.markettwits.profile.presentation.component.edit_profile.data.EditProfileDataStoreBase
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileComponent
import com.markettwits.profile.presentation.component.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import com.markettwits.profile.presentation.component.my_members.MyMembersComponent
import com.markettwits.profile.presentation.component.my_registries.MyRegistriesComponent
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfile
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfileComponent
import com.markettwits.profile.presentation.sign_in.SignInInstanceKeeper
import com.markettwits.profile.presentation.sign_in.SignInScreenComponent
import kotlinx.serialization.Serializable
import ru.alexpanov.core_network.api.StartsRemoteDataSourceImpl
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class DefaultProfileComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext {

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
                        BaseAuthDataSource(
                            remoteService =
                            StartsRemoteDataSourceImpl(
                                HttpClientProvider2(
                                    JsonProvider().get(),
                                    "https://sport-73zoq.ondigitalocean.app"
                                )
                            ),
                            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                            signInMapper = SignInRemoteToUiMapper.Base(),
                            signInCacheMapper = SignInRemoteToCacheMapper.Base()
                        ),
                        toProfile = {
                            navigation.replaceAll(Config.AuthProfile)
                        }
                    ),
                )
            )

            is Config.AuthProfile -> Child.AuthProfile(
                AuthorizedProfileComponent(
                    componentContext,
                    BaseProfileDataSource(
                        BaseAuthDataSource(
                            remoteService =
                            StartsRemoteDataSourceImpl(
                                HttpClientProvider2(
                                    JsonProvider().get(),
                                    "https://sport-73zoq.ondigitalocean.app"
                                )
                            ),
                            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                            signInMapper = SignInRemoteToUiMapper.Base(),
                            signInCacheMapper = SignInRemoteToCacheMapper.Base()
                        )
                    ),
                    event = {
                        handleAuthorizedProfileEvent(it)
                    }
                )
            )

            is Config.UnAuthProfile -> Child.UnAuthProfile(
                UnAuthorizedProfileComponent(
                    componentContext,
                    service = BaseProfileDataSource(
                        BaseAuthDataSource(
                            remoteService =
                            StartsRemoteDataSourceImpl(
                                HttpClientProvider2(
                                    JsonProvider().get(),
                                    "https://sport-73zoq.ondigitalocean.app"
                                )
                            ),
                            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                            signInMapper = SignInRemoteToUiMapper.Base(),
                            signInCacheMapper = SignInRemoteToCacheMapper.Base()
                        )
                    ),
                    goSignIn = {
                        navigation.push(Config.Login)
                    },
                    goAuthProfile = {
                        navigation.replaceCurrent(Config.AuthProfile)
                    }
                )
            )

            is Config.EditProfile -> Child.EditProfile(
                EditProfileComponent(
                    componentContext,
                    userId = config.userId,
                    mapper = RemoteUserToEditProfileMapper.Base(),
                    goBack = ::onBackClicked,
                    service = EditProfileDataStoreBase(
                        RemoteUserToEditProfileMapper.Base(),
                        BaseAuthDataSource(
                            remoteService =
                            StartsRemoteDataSourceImpl(
                                HttpClientProvider2(
                                    JsonProvider().get(),
                                    "https://sport-73zoq.ondigitalocean.app"
                                )
                            ),
                            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                            signInMapper = SignInRemoteToUiMapper.Base(),
                            signInCacheMapper = SignInRemoteToCacheMapper.Base()
                        ),
                        StartsRemoteDataSourceImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                    )
                )
            )

            is Config.MyMembers -> Child.MyMembers(
                MyMembersComponent(componentContext)
            )

            is Config.ChangePassword -> Child.ChangePassword(
                ChangePasswordComponent(componentContext)
            )

            is Config.MyRegistries -> Child.MyRegistries(
                MyRegistriesComponent(componentContext)
            )
        }


    fun onBackClicked() {
        navigation.pop()
    }

    fun handleAuthorizedProfileEvent(event: AuthorizedProfileEvent) {
        when (event) {
            is AuthorizedProfileEvent.SignOut -> navigation.replaceAll(Config.UnAuthProfile)
            is AuthorizedProfileEvent.EditProfile -> navigation.push(Config.EditProfile(event.user.id))
            is AuthorizedProfileEvent.MyRegistries -> navigation.push(Config.MyRegistries)
            is AuthorizedProfileEvent.ChangePasswordProfile -> navigation.push(Config.ChangePassword)
            is AuthorizedProfileEvent.MyMembers -> navigation.push(Config.MyMembers)
        }
    }


    @Serializable
    sealed class Config {

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

    }

    sealed class Child {
        data class Login(val component: SignInScreenComponent) : Child()
        data class AuthProfile(val component: AuthorizedProfile) : Child()
        data class UnAuthProfile(val component: UnAuthorizedProfile) : Child()
        data class EditProfile(val component: com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfile) :
            Child()

        data class ChangePassword(val component: com.markettwits.profile.presentation.component.change_password.ChangePassword) :
            Child()

        data class MyMembers(val component: com.markettwits.profile.presentation.component.my_members.MyMembers) :
            Child()

        data class MyRegistries(val component: com.markettwits.profile.presentation.component.my_registries.MyRegistries) :
            Child()
    }
}