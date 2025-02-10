package com.markettwits.start.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.profile.api.root.RootAuthFlowComponentBase
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationInput
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationOutput
import com.markettwits.start.register.presentation.registration.registration.component.StartRegistrationPageComponentBase
import com.markettwits.start.register.presentation.success.RegisterSuccessComponentBase

/**
 * @param pop callback for navigate to back
 * @param content : first - startId, second - DistanceItem, third -
 *     paymentDisabled, fourth - paymentType
 */

class RootStartRegisterBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val content: StartRegistrationInput
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
        initialConfiguration = RootStartRegister.Config.StartRegistrationPage(
            content
        ),
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
                            RootStartRegister.Config.StartRegistrationPage(
                                content
                            )
                        )
                    }
                )
            )
        }

    private inner class StartRegistrationPageComponentOutputsImpl : StartRegistrationOutput {
        override fun goBack() {
            pop()
        }

        override fun goSuccess() {
            stackNavigation.replaceAll(RootStartRegister.Config.StartRegistrationSuccess)
        }

        override fun goAuth() {
            stackNavigation.replaceAll(RootStartRegister.Config.AuthFlow)
        }
    }
}
