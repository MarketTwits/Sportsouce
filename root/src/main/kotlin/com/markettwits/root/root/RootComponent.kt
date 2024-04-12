package com.markettwits.root.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.root.RootReviewComponent
import com.markettwits.root_profile.RootProfileComponent
import com.markettwits.starts.root.RootStartsComponentBase
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>
    fun navigate(configuration: Configuration)
    @Serializable
    sealed class Configuration {
        @Serializable
        data object Starts : Configuration()
        @Serializable
        data object Review : Configuration()
        @Serializable
        data object Profile : Configuration()
    }

    sealed class Child {
        data class Starts(val component: RootStartsComponentBase) : Child()
        data class Profile(val component: RootProfileComponent) : Child()
        data class Review(val component : RootReviewComponent) : Child()
    }
}