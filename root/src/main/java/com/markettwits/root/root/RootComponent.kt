package com.markettwits.root.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.DefaultProfileComponent
import com.markettwits.root.RootNewsComponent
import com.markettwits.root.RootReviewComponent
import com.markettwits.root.internal.RootStartsComponentBase
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<*, RootComponent.Child>>
    fun navigate(configuration: RootComponent.Configuration)
    @Serializable
    sealed class Configuration {
        @Serializable
        object Starts : Configuration()
        @Serializable
        object News : Configuration()
        @Serializable
        object Review : Configuration()
        @Serializable
        object Profile : Configuration()
    }

    sealed class Child {
        data class Starts(val component: RootStartsComponentBase) : Child()
        data class Profile(val component: DefaultProfileComponent) : Child()
        data class News(val component : RootNewsComponent) : Child()
        data class Review(val component : RootReviewComponent) : Child()
    }
}