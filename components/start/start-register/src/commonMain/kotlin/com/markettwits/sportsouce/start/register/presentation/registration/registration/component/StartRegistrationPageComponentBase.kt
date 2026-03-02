package com.markettwits.sportsouce.start.register.presentation.registration.registration.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.start.register.di.startRegistrationModule
import com.markettwits.sportsouce.start.register.presentation.registration.distance.component.StartDistanceComponentBase
import com.markettwits.sportsouce.start.register.presentation.registration.pay.component.StartPayComponentBase
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.getRegistrationsStage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

class StartRegistrationPageComponentBase(
    componentContext: ComponentContext,
    storeFactory: StartRegistrationPageStoreFactory,
    input: StartRegistrationInput,
    private val output: StartRegistrationOutput
) : StartRegistrationPageComponent, ComponentContext by componentContext {

    private val scope = getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val navigation = StackNavigation<PageConfig>()

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            startId = input.startId,
            distances = input.distances,
            startTitle = input.startTitle,
            paymentType = input.paymentType,
            isPaymentDisabled = input.isPaymentDisabled,
            regType = input.regType,
            comboId = input.comboId
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartRegistrationPageStore.State> = store.stateFlow

    private fun childFactory(
        config: PageConfig,
        componentContext: ComponentContext,
    ) : StartStageComponent {
        return when (config) {
            is PageConfig.Stage -> when (val page = config.page) {
                is StartRegistrationStagePage.Pay -> {
                StartPayComponentBase(
                    componentContext = componentContext,
                    innerState = page.copy(
                        distances = state.value.stages.getRegistrationsStage().map { it.distance },
                    ),
                    goBack = {
                        store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                        navigation.pop()
                    },
                    onEventContent = {
                        store.accept(StartRegistrationPageStore.Intent.OnSendEvent(it))
                    },
                    goSuccess = {
                        output.goSuccess()
                    },
                    storeFactory = scope.get()
                )
            }

                is StartRegistrationStagePage.Registration -> {
                    StartDistanceComponentBase(
                        componentContext = componentContext,
                        innerState = page,
                        onMessage = {
                            store.accept(StartRegistrationPageStore.Intent.OnSendEvent(it))
                        },
                        onOpenMember = { updatedPage, startStatement ->
                            store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(updatedPage))
                            output.openMember(
                                stageId = updatedPage.id,
                                memberId = 1,
                                startStatement = startStatement
                            )
                        },
                        onGoNext = {
                            store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                            navigation.pushNew(
                                PageConfig.Stage(state.value.stages[it.id + 1])
                            )
                        },
                        onGoBack = {
                            navigation.pop()
                            store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                        }
                    )
                }

                is StartRegistrationStagePage.Empty -> StartStageComponent.Empty
            }
        }
    }

        override val pages: Value<ChildStack<*, StartStageComponent>> = childStack(
            source = navigation,
            serializer = PageConfig.serializer(),
            initialConfiguration = PageConfig.Stage(StartRegistrationStagePage.Empty),
            handleBackButton = true,
            childFactory = ::childFactory
        )

    override fun obtainEvent(intent: StartRegistrationPageStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is StartRegistrationPageStore.Label.GoBack -> output.goBack()

                is StartRegistrationPageStore.Label.OnPageSelected -> {
                    val selectedIndex = it.pageIndex.coerceIn(0, it.items.lastIndex)
                    val stagePages = it.items
                        .take(selectedIndex + 1)
                        .map { page -> PageConfig.Stage(page) }
                        .toTypedArray()
                    navigation.replaceAll(*stagePages)
                }

                is StartRegistrationPageStore.Label.GoAuth -> output.goAuth()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }

    @Serializable
    private sealed interface PageConfig {
        @Serializable
        data class Stage(val page: StartRegistrationStagePage) : PageConfig
    }
}
