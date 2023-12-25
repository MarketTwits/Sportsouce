package com.markettwits.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.api.TimeApiImpl
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.start.core.BaseTimeMapper
import com.markettwits.start.data.BaseStartDataSource
import com.markettwits.start.data.StartRemoteToUiMapper
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.StartScreenComponent
import com.markettwits.starts.data.BaseStartsDataSource
import com.markettwits.starts.data.StartsCloudToUiMapper
import kotlinx.serialization.Serializable
import ru.alexpanov.core_network.api.StartsRemoteDataSourceImpl
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class DefaultStartsComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _configStack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Starts,
            handleBackButton = true,
            childFactory = ::child,
        )

    val childStack: Value<ChildStack<*, Child>> get() = _configStack

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): Child =
        when (config) {
            is Config.Start -> Child.Start(
                StartScreenComponent(
                    componentContext,
                    config.startId,
                    BaseStartDataSource(
                        service = StartsRemoteDataSourceImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                        authService = BaseAuthDataSource(
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
                        timeService = TimeApiImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://timeapi.io"
                            )
                        ),
                        mapper = StartRemoteToUiMapper.Base(BaseTimeMapper()),
                    ),
                    back = {
                        onBackClicked()
                    },
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list)
                    }
                )
            )

            is Config.Starts ->
                Child.Starts(StartsScreenComponent(
                    componentContext = componentContext,
                    dataSource = BaseStartsDataSource(
                        StartsRemoteDataSourceImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                        StartsCloudToUiMapper.Base(
                            BaseTimeMapper()
                        )
                    ),
                    toDetail = {
                        onItemClick(it)
                    }
                ))

            is Config.StartMembers -> Child.StartMembers(
                StartMembersScreenComponent(
                    componentContext = componentContext,
                    service = BaseStartDataSource(
                        StartsRemoteDataSourceImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                        authService = BaseAuthDataSource(
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
                        mapper = StartRemoteToUiMapper.Base(BaseTimeMapper()),
                        timeService = TimeApiImpl(
                            HttpClientProvider2(
                                JsonProvider().get(),
                                "https://timeapi.io"
                            )
                        ),
                    ),
                    startId = config.startId,
                    membersUi = config.items,
                    openFilterScreen = {
                        openMembersFilter(config.items)
                    },
                    onBack = {
                        onBackClicked()
                    }
                ),
            )

            is Config.StartMembersFilter -> Child.StartMembersFilter(
                StartMembersFilterScreenComponent(
                    context = componentContext,
                    items = config.items,
                    back = {
                        onBackClicked()
                    }
                )
            )
        }


    fun onBackClicked() {
        navigation.pop()
    }


    @Serializable
    sealed class Config {
        @Serializable
        data class Start(
            val startId: Int,
            val isBackEnabled: Boolean,
        ) : Config()

        @Serializable
        data object Starts : Config()

        @Serializable
        data class StartMembers(val startId: Int, val items: List<StartMembersUi>) : Config()

        @Serializable
        data class StartMembersFilter(val items: List<StartMembersUi>) : Config()
    }

    sealed class Child {
        data class Start(val component: StartScreenComponent) : Child()
        data class Starts(val component: StartsScreenComponent) : Child()
        data class StartMembers(val component: StartMembersScreenComponent) : Child()
        data class StartMembersFilter(val component: StartMembersFilterScreen) : Child()
    }

    fun onItemClick(startdId: Int) {
        navigation.push(Config.Start(startdId, true))
    }

    fun openMembersScreen(startId: Int, items: List<StartMembersUi>) {
        navigation.push(Config.StartMembers(startId, items))
    }

    fun openMembersFilter(items: List<StartMembersUi>) {
        navigation.push(Config.StartMembersFilter(items))
    }

}