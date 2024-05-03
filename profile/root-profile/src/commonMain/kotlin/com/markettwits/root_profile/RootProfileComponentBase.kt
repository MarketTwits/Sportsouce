package com.markettwits.root_profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.profile.api.root.RootAuthFlowComponentBase
import com.markettwits.profile.root.RootAuthorizedProfileComponentBase
import com.markettwits.unauthorized.di.unAuthorizedModule
import com.markettwits.unauthorized.presentation.component.UnAuthorizedProfileComponentBase

class RootProfileComponentBase(
    componentContext: ComponentContext,
) :
    ComponentContext by componentContext, RootProfileComponent {

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
                    goProfile = { navigation.replaceAll(RootProfileComponent.Config.AuthProfile) }
                )
            )
        }
}