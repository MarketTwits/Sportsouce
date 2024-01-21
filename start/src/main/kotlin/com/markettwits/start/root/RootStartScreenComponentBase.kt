package com.markettwits.start.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.start.presentation.membres.filter_screen.HandleMembersFilterBase
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.membres.list.filter.MembersFilterBase
import com.markettwits.start.presentation.start.StartScreenComponentComponentBase
import com.markettwits.start.di.startModule
import com.markettwits.start.di.startRegistrationModule
import com.markettwits.start.presentation.comments.StartCommentsComponentBase
import com.markettwits.start.presentation.comments.StartCommentsStoreFactory
import com.markettwits.start.presentation.registration.StartRegistrationComponentBase

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val pop: () -> Unit
) :
    RootStartScreenComponent,
    ComponentContext by context {
    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startModule, startRegistrationModule)
    )
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
                StartScreenComponentComponentBase(
                    componentContext = componentContext,
                    startId = config.startId,
                    back = pop::invoke,
                    register = { distanceInfo ->
                        navigation.push(
                            RootStartScreenComponent.Config.StartRegistration(
                                startId = startId,
                                distanceInfo = distanceInfo
                            )
                        )
                    },
                    storeFactory = scope.get(),
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, emptyList())
                    }
                ),
                StartCommentsComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    storeFactory = StartCommentsStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        service = scope.get(),
                    )
                )
            )

            is RootStartScreenComponent.Config.StartMembers -> RootStartScreenComponent.Child.StartMembers(
                StartMembersScreenComponent(
                    componentContext = componentContext,
                    membersUi = config.items,
                    openFilterScreen = ::openMembersFilter,
                    onBack = navigation::pop,
                    membersFilter = MembersFilterBase()
                ),
            )

            is RootStartScreenComponent.Config.StartMembersFilter -> RootStartScreenComponent.Child.StartMembersFilter(
                StartMembersFilterScreenComponent(
                    context = componentContext,
                    items = config.items,
                    handleMembersFilter = HandleMembersFilterBase(),
                    apply = { filter ->
                        navigation.pop { // Pop ItemDetailsComponent
                            (childStack.value.active.instance as? RootStartScreenComponent.Child.StartMembers)?.component?.updateFilter(
                                filter = filter
                            )
                        }
                    },
                    back = navigation::pop
                )
            )

            is RootStartScreenComponent.Config.StartRegistration -> RootStartScreenComponent.Child.StartRegistration(
                StartRegistrationComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    distanceInfo = config.distanceInfo,
                    storeFactory = scope.get(),
                    pop = navigation::pop
                )
            )
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