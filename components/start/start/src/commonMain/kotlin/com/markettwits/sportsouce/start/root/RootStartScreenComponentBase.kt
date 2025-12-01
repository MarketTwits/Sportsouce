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
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStoreFactory
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreenComponent
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponentBase
import com.markettwits.sportsouce.start.presentation.series.component.StartSeriesComponentBase
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenComponentBase
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
                val supportComponent = StartSupportComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                )
                RootStartScreenComponent.Child.Start(
                    component = StartScreenComponentBase(
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
                        membersResult = { startId, membersResult ->
                            navigation.pushNew(
                                RootStartScreenComponent.Config.StartMembersResult(startId, membersResult)
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
                            supportComponent.obtainEvent(StartSupportStore.Intent.ApplyStartId(it))
                        },
                        onOpenStartCommentsScreen = { startId, mode ->
                            navigation.pushNew(RootStartScreenComponent.Config.StartComments(startId, mode))
                        },
                        relatedStarts = { startId, relatedStarts ->
                            navigation.pushNew(RootStartScreenComponent.Config.RelatedStarts(relatedStarts, startId))
                        }
                    ),
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
                    repository = scope.get(),
                    memberResult = config.items,
                    startId = config.startId,
                    goBack = navigation::pop,
                )
            )

            is RootStartScreenComponent.Config.StartComments -> {
                val commentsStoreFactory = StartCommentsStoreFactory(
                    storeFactory = DefaultStoreFactory(),
                    service = scope.get(),
                )

                val component = StartCommentsComponentBase(
                    context = componentContext,
                    storeFactory = commentsStoreFactory,
                    startId = config.startId,
                    mode = config.mode,
                    onBack = navigation::pop,
                    onNavigateToReplies = { commentId: Int, replier: String, commentData: com.markettwits.sportsouce.start.presentation.start.component.CommentData ->
                        navigation.pushNew(
                            RootStartScreenComponent.Config.StartComments(
                                startId = config.startId,
                                mode = CommentMode.Reply(
                                    replier = replier,
                                    messageId = commentId,
                                    parentComment = commentData
                                )
                            )
                        )
                    }
                )
                RootStartScreenComponent.Child.StartComments(component)
            }
            is RootStartScreenComponent.Config.RelatedStarts -> RootStartScreenComponent.Child.StartSeries(
                StartSeriesComponentBase(
                    componentContext = componentContext,
                    storeFactory = DefaultStoreFactory(),
                    items = config.starts,
                    currentStartId = config.currentStartId,
                    onClickBack = {
                        navigation.pop()
                    },
                    onClickStart = {
                        navigation.pushNew(
                            RootStartScreenComponent.Config.Start(
                                StartScreenInput.Item(it)
                            )
                        )
                    }
                )
            )
        }
}