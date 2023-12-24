package com.markettwits.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
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
            initialConfiguration = Config.Profile(ProfileLaunchPolicy.Base),
            handleBackButton = true,
            childFactory = ::child,
        )

    val childStack: Value<ChildStack<*, Child>> get() = _configStack

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): Child =
        when (config) {
            is Config.Profile -> Child.Profile(
                ProfileScreenComponent(
                    context = componentContext,
                    profileInstanceKeeper = ProfileInstanceKeeper(
                        BaseProfileDataSource(
                            BaseAuthDataSource(
                                remoteService =
                                StartsRemoteDataSourceImpl(HttpClientProvider2(JsonProvider().get())),
                                local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                                signInMapper = SignInRemoteToUiMapper.Base(),
                                signInCacheMapper = SignInRemoteToCacheMapper.Base()
                            )
                        ),
                        goToAuth = {
                            navigation.push(Config.Login)
                        }
                    ),
                    launchPolicy = config.launchPolicy
                )
            )

            is Config.Login -> Child.Login(
                SignInScreenComponent(
                    context = componentContext,
                    signInInstanceKeeper = SignInInstanceKeeper(
                        BaseAuthDataSource(
                            remoteService =
                            StartsRemoteDataSourceImpl(HttpClientProvider2(JsonProvider().get())),
                            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                            signInMapper = SignInRemoteToUiMapper.Base(),
                            signInCacheMapper = SignInRemoteToCacheMapper.Base()
                        ),
                        toProfile = {
                            navigation.replaceAll(Config.Profile(ProfileLaunchPolicy.Update))
                            //navigation.bringToFront(Config.Profile)
                        }
                    ),
                )
            )
        }


    fun onBackClicked() {
        navigation.pop()
    }


    @Serializable
    sealed class Config {

        @Serializable
        data class Profile(val launchPolicy: ProfileLaunchPolicy) : Config()

        @Serializable
        data object Login : Config()
    }

    sealed class Child {
        data class Profile(val component: ProfileScreenComponent) : Child()
        data class Login(val component: SignInScreenComponent) : Child()
    }
}