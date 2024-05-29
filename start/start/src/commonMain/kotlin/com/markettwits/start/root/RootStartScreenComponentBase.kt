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
import com.markettwits.start.di.startModule
import com.markettwits.start.presentation.album.di.startAlbumModule
import com.markettwits.start.presentation.album.presentation.component.StartAlbumComponentBase
import com.markettwits.start.presentation.comments.comments.component.StartCommentsComponentBase
import com.markettwits.start.presentation.comments.comments.store.StartCommentsStoreFactory
import com.markettwits.start.presentation.membres.filter_screen.HandleMembersFilterBase
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.component.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.filter.MembersFilterBase
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.start.component.StartScreenComponentComponentBase
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start_support.di.startSupportModule
import com.markettwits.start_support.presentation.component.StartSupportComponentBase

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val pop: () -> Unit
) : RootStartScreenComponent,
    ComponentContext by context {
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
                component = StartScreenComponentComponentBase(
                    componentContext = componentContext,
                    startId = config.startId,
                    back = pop::invoke,
                    register = { distanceInfo, paymentDisabled, paymentType, startTitle ->
                        navigation.push(
                            RootStartScreenComponent.Config.StartRegistration(
                                startId = startId,
                                distanceInfo = distanceInfo,
                                paymentDisabled = paymentDisabled,
                                paymentType = paymentType,
                                startTitle = startTitle
                            )
                        )
                    },
                    storeFactory = scope.get(),
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, emptyList())
                    },
                    album = {
                        navigation.push(RootStartScreenComponent.Config.StartAlbum(it))
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
                    content = RootStartRegister.StartRegisterParams(
                        config.startId,
                        config.distanceInfo,
                        config.paymentDisabled,
                        config.paymentType,
                        config.startTitle
                    )
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
        }

    private fun openMembersScreen(
        startId: Int,
        items: List<StartMembersUi>,
        filter: List<MembersFilterGroup>
    ) {
        navigation.push(RootStartScreenComponent.Config.StartMembers(startId, items, filter))
    }

    private fun openMembersFilter(items: List<MembersFilterGroup>) {
        navigation.push(RootStartScreenComponent.Config.StartMembersFilter(items))
    }
}