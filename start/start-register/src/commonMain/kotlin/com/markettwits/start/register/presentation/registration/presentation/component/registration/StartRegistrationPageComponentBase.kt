package com.markettwits.start.register.presentation.registration.presentation.component.registration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.getOrCreateKoinScope
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start.register.presentation.registration.presentation.component.StartStageComponent
import com.markettwits.start.register.presentation.registration.presentation.component.distance.StartDistanceComponentBase
import com.markettwits.start.register.presentation.registration.presentation.component.pay.StartPayComponentBase
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.components.registration.getRegistrationsStage
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalDecomposeApi::class)
class StartRegistrationPageComponentBase(
    componentContext: ComponentContext,
    storeFactory: StartRegistrationPageStoreFactory,
    input: StartRegistrationInput,
    output: StartRegistrationOutput
) : StartRegistrationPageComponent, ComponentContext by componentContext {

    private val scope = getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val navigation = StackNavigation<StartRegistrationStagePage>()

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            startId = input.startId,
            distances = input.distances,
            startTitle = input.startTitle,
            paymentType = input.paymentType,
            isPaymentDisabled = input.isPaymentDisabled,
            comboId = input.comboId
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartRegistrationPageStore.State> = store.stateFlow

    private fun childFactory(
        page : StartRegistrationStagePage,
        componentContext: ComponentContext,
    ) : StartStageComponent {
        return when (page) {
            is StartRegistrationStagePage.Pay -> {
                StartPayComponentBase(
                    componentContext = componentContext,
                    innerState = page.copy(
                        distances = state.value.stages.getRegistrationsStage().map { it.distance },
                        startInfo = state.value.registrationInfo
                    ),
                    goBack = {
                        store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                        navigation.pop()
                    },
                    onEventContent = {
                        store.accept(StartRegistrationPageStore.Intent.OnSendEvent(it))
                    },
                    repository = scope.get(), intentAction = scope.get()
                )
            }

            is StartRegistrationStagePage.Registration -> {
                StartDistanceComponentBase(
                    componentContext = componentContext,
                    innerState = page.copy(

                    ),
                    onMessage = {
                        store.accept(StartRegistrationPageStore.Intent.OnSendEvent(it))
                    },
                    onGoNext = {
                        store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                        navigation.pushNew(
                            state.value.stages[it.id + 1]
                        )
                    },
                    onGoBack = {
                        navigation.pop()
                        store.accept(StartRegistrationPageStore.Intent.UpdateStagePage(it))
                    }
                )
            }

            StartRegistrationStagePage.Empty -> StartStageComponent.Empty
        }
    }

        override val pages: Value<ChildStack<*, StartStageComponent>> = childStack(
            source = navigation,
            serializer = StartRegistrationStagePage.serializer(),
            initialConfiguration = StartRegistrationStagePage.Empty,
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
                    navigation.replaceAll(it.items[it.pageIndex])
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }
}