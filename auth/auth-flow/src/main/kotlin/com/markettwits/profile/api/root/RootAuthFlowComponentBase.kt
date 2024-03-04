package com.markettwits.profile.api.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.profile.internal.forgot_password.di.forgotPasswordModule
import com.markettwits.profile.internal.forgot_password.presentation.component.ForgotPasswordComponentBase
import com.markettwits.profile.internal.sign_in.di.signInModule
import com.markettwits.profile.internal.sign_in.presentation.component.SignInScreenComponent
import com.markettwits.profile.internal.sign_up.di.signUpModule
import com.markettwits.profile.internal.sign_up.presentation.component.SignUpComponentBase


class RootAuthFlowComponentBase(
    context: ComponentContext,
    private val goProfile: () -> Unit
) : RootAuthFlowComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootAuthFlowComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(signInModule, signUpModule, forgotPasswordModule)
    )

    override val childStack: Value<ChildStack<*, RootAuthFlowComponent.Child>> = childStack(
        source = navigation,
        serializer = RootAuthFlowComponent.Config.serializer(),
        initialConfiguration = RootAuthFlowComponent.Config.SignIn,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootAuthFlowComponent.Config,
        componentContext: ComponentContext,
    ): RootAuthFlowComponent.Child =
        when (config) {
            is RootAuthFlowComponent.Config.ForgotPassword -> RootAuthFlowComponent.Child.ForgotPassword(
                ForgotPasswordComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get()
                )
            )
            is RootAuthFlowComponent.Config.SignIn -> RootAuthFlowComponent.Child.SignIn(
                SignInScreenComponent(
                    context = componentContext,
                    signInInstanceKeeper = scope.get(),
                    toSignUp = { navigation.push(RootAuthFlowComponent.Config.SignUp) },
                    toProfile = { goProfile() },
                    toForgotPassword = {
                        navigation.push(RootAuthFlowComponent.Config.ForgotPassword)
                    }
                )
            )

            is RootAuthFlowComponent.Config.SignUp -> RootAuthFlowComponent.Child.SignUp(
                SignUpComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    pop = navigation::pop,
                    profile = { goProfile() }
                )
            )
        }
}