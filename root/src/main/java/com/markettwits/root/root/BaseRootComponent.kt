package com.markettwits.root.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.DefaultProfileComponent
import com.markettwits.root.RootNewsComponent
import com.markettwits.root.RootNewsComponentBase
import com.markettwits.root.RootReviewComponentBase
import com.markettwits.starts.DefaultStartsComponent

class BaseRootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {
    private val navigation = StackNavigation<RootComponent.Configuration>()

    private val stack =
        childStack(
            source = navigation,
            serializer = RootComponent.Configuration.serializer(),
            initialStack = { listOf(RootComponent.Configuration.Starts) },
            childFactory = ::createChild,
        )
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = stack
    override fun navigate(configuration: RootComponent.Configuration) {
        navigation.bringToFront(configuration)
    }

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.Starts -> RootComponent.Child.Starts(
                DefaultStartsComponent(componentContext = componentContext)
            )

            is RootComponent.Configuration.News -> RootComponent.Child.News(
                newsComponent(
                    componentContext
                )
            )

            is RootComponent.Configuration.Profile -> RootComponent.Child.Profile(
                DefaultProfileComponent(componentContext = componentContext)
            )

            is RootComponent.Configuration.Review -> RootComponent.Child.Review(
                component = RootReviewComponentBase(
                    context = componentContext,

                ),
            )
        }

    private fun newsComponent(componentContext: ComponentContext): RootNewsComponent =
        RootNewsComponentBase(context = componentContext)
}