package com.markettwits.root.root

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.markettwits.root.RootReviewComponentBase
import com.markettwits.root_profile.RootProfileComponentBase
import com.markettwits.starts.root.internal.RootStartsComponentBase

class RootComponentBase(
    componentContext: ComponentContext,
    private val context: Context,
) : ComponentContext by componentContext, RootComponent {


    private val navigation = StackNavigation<RootComponent.Configuration>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = RootComponent.Configuration.serializer(),
        initialStack = { listOf(RootComponent.Configuration.Starts) },
        childFactory = ::createChild,
    )

    override fun navigate(configuration: RootComponent.Configuration) {
        navigation.bringToFront(configuration)
    }

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.Starts -> RootComponent.Child.Starts(
                RootStartsComponentBase(
                    componentContext = componentContext,
                    context = context
                )
            )

            is RootComponent.Configuration.Profile -> RootComponent.Child.Profile(
                RootProfileComponentBase(componentContext = componentContext)
            )

            is RootComponent.Configuration.Review -> RootComponent.Child.Review(
                component = RootReviewComponentBase(
                    context = componentContext
                )
            )
        }
}