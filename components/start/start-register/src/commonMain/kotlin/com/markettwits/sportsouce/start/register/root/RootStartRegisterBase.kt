package com.markettwits.sportsouce.start.register.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.start.register.di.startRegistrationModule
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesComponentBase
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesOutput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationInput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationOutput
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationPageComponentBase
import com.markettwits.sportsouce.start.register.presentation.success.RegisterSuccessComponentBase

/**
 * @param pop callback for navigate to back
 * @param content : first - startId, second - DistanceItem, third -
 *     paymentDisabled, fourth - paymentType
 */

class RootStartRegisterBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val input: StartDistancesInput,
) : ComponentContext by componentContext, RootStartRegister {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext(false)
    }
    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val stackNavigation = StackNavigation<RootStartRegister.Config>()

    override val childStack: Value<ChildStack<*, RootStartRegister.Child>> = childStack(
        source = stackNavigation,
        serializer = RootStartRegister.Config.serializer(),
        initialConfiguration = getInitialConfig(input),
        handleBackButton = true,
        childFactory = ::childStack,
    )

    private fun childStack(
        config: RootStartRegister.Config,
        componentContext: ComponentContext,
    ): RootStartRegister.Child =
        when (config) {

            RootStartRegister.Config.StartRegistrationSuccess -> RootStartRegister.Child.StartRegistrationSuccess(
                RegisterSuccessComponentBase(
                    componentComponent = componentContext,
                    next = pop::invoke
                )
            )

            is RootStartRegister.Config.StartRegistrationPage -> RootStartRegister.Child.StartRegistrationPage(
                StartRegistrationPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    input = config.input,
                    output = StartRegistrationPageComponentOutputsImpl()
                )
            )

            is RootStartRegister.Config.AuthFlow -> RootStartRegister.Child.AuthFlow(
                RootAuthFlowComponentBase(
                    context = componentContext,
                    goBack = pop::invoke,
                    goProfile = {
                        stackNavigation.replaceAll(
                            RootStartRegister.Config.StartRegistrationDistancesPage(
                                input
                            )
                        )
                    }
                )
            )

            is RootStartRegister.Config.StartRegistrationDistancesPage -> RootStartRegister.Child.StartRegistrationDistances(
                StartDistancesComponentBase(
                    componentContext = componentContext,
                    input = config.input,
                    output = StartDistancesOutputImpl()
                )
            )
        }

    private inner class StartRegistrationPageComponentOutputsImpl : StartRegistrationOutput {
        override fun goBack() {
            stackNavigation.pop()
        }

        override fun goSuccess() {
            stackNavigation.replaceAll(RootStartRegister.Config.StartRegistrationSuccess)
        }

        override fun goAuth() {
            stackNavigation.replaceAll(RootStartRegister.Config.AuthFlow)
        }
    }

    private inner class StartDistancesOutputImpl : StartDistancesOutput {
        override fun onClickGoBack() {
            pop()
        }

        override fun onClickDistance(distinctDistance: StartRegistrationInput) {
            stackNavigation.pushNew(
                RootStartRegister.Config.StartRegistrationPage(
                    distinctDistance
                )
            )
        }

    }

    private fun getInitialConfig(input: StartDistancesInput): RootStartRegister.Config {
        return RootStartRegister.Config.StartRegistrationDistancesPage(input)
    }
}
