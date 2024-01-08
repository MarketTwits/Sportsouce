package com.markettwits.start.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
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
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.membres.list.filter.MembersFilterBase
import com.markettwits.start.presentation.start.StartScreenComponent
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val pop: () -> Unit
) :
    RootStartScreenComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartScreenComponent.Config>()
    override val childStack: Value<ChildStack<*, RootStartScreenComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootStartScreenComponent.Config.serializer(),
            initialConfiguration = RootStartScreenComponent.Config.Start(startId, true),
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: RootStartScreenComponent.Config,
        componentContext: ComponentContext,
    ): RootStartScreenComponent.Child =
        when (config) {
            is RootStartScreenComponent.Config.Start -> RootStartScreenComponent.Child.Start(
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
                       pop()
                    },
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, emptyList())
                    }
                )
            )


            is RootStartScreenComponent.Config.StartMembers -> RootStartScreenComponent.Child.StartMembers(
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

            is RootStartScreenComponent.Config.StartMembersFilter -> RootStartScreenComponent.Child.StartMembersFilter(
                StartMembersFilterScreenComponent(
                    context = componentContext,
                    items = config.items,
                    back = {
                        onBackClicked()
                    },
                    apply = { filter ->
                        navigation.pop { // Pop ItemDetailsComponent
                            (childStack.value.active.instance as? RootStartScreenComponent.Child.StartMembers)?.component?.updateFilter(
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

    fun openMembersScreen(
        startId: Int,
        items: List<StartMembersUi>,
        filter: List<MembersFilterGroup>
    ) {
        navigation.push(RootStartScreenComponent.Config.StartMembers(startId, items, filter))
    }

    fun openMembersFilter(items: List<MembersFilterGroup>) {
        navigation.push(RootStartScreenComponent.Config.StartMembersFilter(items))
    }
}