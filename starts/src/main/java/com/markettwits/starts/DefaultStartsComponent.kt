package com.markettwits.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.api.TimeApiImpl
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.start.data.BaseStartDataSource
import com.markettwits.start.data.StartMembersToUiMapper
import com.markettwits.start.data.StartRemoteToUiMapper
import com.markettwits.start.presentation.membres.filter_screen.HandleMembersFilterBase
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.membres.list.filter.MembersFilterBase
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
                        mapper = StartRemoteToUiMapper.Base(
                            mapper = BaseTimeMapper(),
                            membersToUiMapper = StartMembersToUiMapper.Base()
                        )
                    ),
                    back = {
                        onBackClicked()
                    },
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, emptyList())
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
                    membersUi = config.items,
                    openFilterScreen = {
                        openMembersFilter(it)
                    },
                    onBack = {
                        onBackClicked()
                    },
                    membersFilter = MembersFilterBase()
                ),
            )

            is Config.StartMembersFilter -> Child.StartMembersFilter(
                StartMembersFilterScreenComponent(
                    context = componentContext,
                    items = config.items,
                    back = {
                        onBackClicked()
                    },
                    apply = { filter ->
                        navigation.pop { // Pop ItemDetailsComponent
                            (childStack.value.active.instance as? Child.StartMembers)?.component?.updateFilter(
                                filter = filter
                            )
                        }
                    },
                    handleMembersFilter = HandleMembersFilterBase()
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
        data class StartMembers(
            val startId: Int,
            val items: List<StartMembersUi>,
            val filter: List<MembersFilterGroup>
        ) : Config()

        @Serializable
        data class StartMembersFilter(val items: List<MembersFilterGroup>) : Config()
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

    fun openMembersScreen(
        startId: Int,
        items: List<StartMembersUi>,
        filter: List<MembersFilterGroup>
    ) {
        navigation.push(Config.StartMembers(startId, items, filter))
    }

    fun openMembersFilter(items: List<MembersFilterGroup>) {
        navigation.push(Config.StartMembersFilter(items))
    }

}
