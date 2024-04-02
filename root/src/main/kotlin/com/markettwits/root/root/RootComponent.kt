package com.markettwits.root.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.component.base.DefaultProfileComponent
import com.markettwits.root.RootReviewComponent
import com.markettwits.starts.root.internal.RootStartsComponentBase
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
        data class Profile(val component: DefaultProfileComponent) : Child()
        data class Review(val component : RootReviewComponent) : Child()
    }
}