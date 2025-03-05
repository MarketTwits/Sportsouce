package com.markettwits.start.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.start.di.startModule
import com.markettwits.start.presentation.album.di.startAlbumModule
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponentBase
import com.markettwits.start.presentation.comments.component.StartCommentsComponentBase
import com.markettwits.start.presentation.comments.store.StartCommentsStoreFactory
import com.markettwits.start.presentation.membres.filter.HandleMembersFilterBase
import com.markettwits.start.presentation.membres.filter.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.component.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.models.MembersFilterBase
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.component.StartMemberResultsComponentBase
import com.markettwits.start.presentation.start.component.StartScreenComponentComponentBase
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start_support.di.startSupportModule
import com.markettwits.start_support.presentation.component.StartSupportComponentBase

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val pop: () -> Unit,
) : RootStartScreenComponent, ComponentContext by context {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext(false)
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startModule, startRegistrationModule, startAlbumModule, startSupportModule)
    )
    private val navigation = StackNavigation<RootStartScreenComponent.Config>()

    override val childStack: Value<ChildStack<*, RootStartScreenComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootStartScreenComponent.Config.serializer(),
            initialConfiguration = RootStartScreenComponent.Config.Start(startId),
            handleBackButton = true,
            key = startId.toString(),
            childFactory = ::child,
        )

    private fun child(
        config: RootStartScreenComponent.Config,
        componentContext: ComponentContext,
    ): RootStartScreenComponent.Child =
        when (config) {
            is RootStartScreenComponent.Config.Start -> RootStartScreenComponent.Child.Start(
                component = StartScreenComponentComponentBase(
                    componentContext = componentContext,
                    startId = config.startId,
                    back = {
                        if (config.index == 0) {
                            pop()
                        } else {
                            navigation.popTo(config.index - 1)
                        }
                    },
                    storeFactory = scope.get(),
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, filter = emptyList())
                    },
                    album = {
                        navigation.pushNew(RootStartScreenComponent.Config.StartAlbum(it))
                    },
                    registerNew = {
                        navigation.pushNew(
                            RootStartScreenComponent.Config.StartRegistration(it)
                        )
                    },
                    membersResult = {
                        navigation.pushNew(
                            RootStartScreenComponent.Config.StartMembersResult(it)
                        )
                    },
                    pushStart = {
                        navigation.pushNew(RootStartScreenComponent.Config.Start(it, config.index + 1))
                    }
                ),
                commentsComponent = StartCommentsComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    storeFactory = StartCommentsStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        service = scope.get(),
                    )
                ),
                supportComponent = StartSupportComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    startId = config.startId
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
                RootStartRegisterBase(
                    componentContext = componentContext,
                    pop = navigation::pop,
                    input = config.input,
                )
            )

            is RootStartScreenComponent.Config.StartAlbum -> RootStartScreenComponent.Child.StartAlbum(
                StartAlbumComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = navigation::pop,
                    images = config.images
                )
            )

            is RootStartScreenComponent.Config.StartMembersResult -> RootStartScreenComponent.Child.StartMembersResults(
                StartMemberResultsComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    memberResult = config.items,
                    goBack = navigation::pop,
                )
            )
        }

    private fun openMembersScreen(
        startId: Int,
        items: List<StartMembersUi>,
        filter: List<MembersFilterGroup>
    ) {
        navigation.pushNew(RootStartScreenComponent.Config.StartMembers(startId, items, filter))
    }

    private fun openMembersFilter(items: List<MembersFilterGroup>) {
        navigation.pushNew(RootStartScreenComponent.Config.StartMembersFilter(items))
    }
}