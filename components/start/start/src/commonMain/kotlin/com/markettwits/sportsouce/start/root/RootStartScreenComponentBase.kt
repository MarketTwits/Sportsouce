package com.markettwits.sportsouce.start.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.start.di.startModule
import com.markettwits.sportsouce.start.presentation.album.di.startAlbumModule
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponentBase
import com.markettwits.sportsouce.start.presentation.comments.component.StartCommentsComponentBase
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStoreFactory
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreenComponent
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenComponentComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.register.di.startRegistrationModule
import com.markettwits.sportsouce.start.register.root.RootStartRegisterBase
import com.markettwits.sportsouce.start.support.di.startSupportModule
import com.markettwits.sportsouce.start.support.presentation.component.StartSupportComponentBase
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val input: StartScreenInput,
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
            initialConfiguration = RootStartScreenComponent.Config.Start(input),
            handleBackButton = true,
            key = input.toString(),
            childFactory = ::child,
        )

    private fun child(
        config: RootStartScreenComponent.Config,
        componentContext: ComponentContext,
    ): RootStartScreenComponent.Child =
        when (config) {
            is RootStartScreenComponent.Config.Start -> {
                val commentsComponent = StartCommentsComponentBase(
                    context = componentContext,
                    storeFactory = StartCommentsStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        service = scope.get(),
                    )
                )
                val supportComponent = StartSupportComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                )
                RootStartScreenComponent.Child.Start(
                    component = StartScreenComponentComponentBase(
                        componentContext = componentContext,
                        input = config.startScreenInput,
                        back = {
                            if (config.index == 0) {
                                pop()
                            } else {
                                navigation.popTo(config.index - 1)
                            }
                        },
                        storeFactory = scope.get(),
                        members = { id: Int, list: List<StartMembersUi> ->
                            navigation.pushNew(RootStartScreenComponent.Config.StartMembers(id, list))
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
                        pushStart = { item ->
                            navigation.pushNew(
                                RootStartScreenComponent.Config.Start(
                                    StartScreenInput.Item(item),
                                    config.index + 1
                                )
                            )
                        },
                        onApplyStartId = {
                            commentsComponent.obtainEvent(StartCommentsStore.Intent.ApplyStartId(it))
                            supportComponent.obtainEvent(StartSupportStore.Intent.ApplyStartId(it))
                        }
                    ),
                    commentsComponent = commentsComponent,
                    supportComponent = supportComponent)
            }

            is RootStartScreenComponent.Config.StartMembers -> RootStartScreenComponent.Child.StartMembers(
                StartMembersScreenComponent(
                    componentContext = componentContext,
                    startId = config.startId,
                    initialMembersUi = config.items,
                    onBack = navigation::pop,
                    storeFactory = scope.get(),
                    repository = scope.get()
                ),
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

}