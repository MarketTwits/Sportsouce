package com.markettwits.sportsouce.root_profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.profile.authorized.root.RootAuthorizedProfileComponentBase
import com.markettwits.sportsouce.unauthorized.di.unAuthorizedModule
import com.markettwits.sportsouce.unauthorized.presentation.component.UnAuthorizedProfileComponentBase

class RootProfileComponentBase(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootProfileComponent {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(unAuthorizedModule)
    )
    private val navigation = StackNavigation<RootProfileComponent.Config>()

    override val childStack =
        childStack(
            source = navigation,
            serializer = RootProfileComponent.Config.serializer(),
            initialConfiguration = RootProfileComponent.Config.UnAuthProfile,
            handleBackButton = true,
            childFactory = ::child,
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: RootProfileComponent.Config,
        componentContext: ComponentContext,
    ): RootProfileComponent.Child =
        when (config) {
            is RootProfileComponent.Config.AuthProfile -> RootProfileComponent.Child.AuthProfile(
                RootAuthorizedProfileComponentBase(
                    componentContext = componentContext,
                    signOut = {
                        navigation.replaceAll(RootProfileComponent.Config.UnAuthProfile)
                    }
                )
            )

            is RootProfileComponent.Config.UnAuthProfile -> RootProfileComponent.Child.UnAuthProfile(
                UnAuthorizedProfileComponentBase(
                    context = componentContext,
                    useCase = scope.get(),
                    goSignIn = {
                        navigation.push(RootProfileComponent.Config.AuthFlow)
                    },
                    goAuthProfile = {
                        navigation.replaceCurrent(RootProfileComponent.Config.AuthProfile)
                    }
                )
            )

            is RootProfileComponent.Config.AuthFlow -> RootProfileComponent.Child.AuthFlow(
                RootAuthFlowComponentBase(
                    context = componentContext,
                    goProfile = { navigation.replaceAll(RootProfileComponent.Config.AuthProfile) },
                    goBack = { navigation.pop() }
                )
            )
        }
}