package com.markettwits.popular.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.popular.popular.presentation.component.StartsPopularComponent
import com.markettwits.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartsPopularComponent {
    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {
        @Serializable
        data object Popular : Config

        @Serializable
        data class Start(val startId: Int) : Config
    }

    sealed interface Child {
        data class Popular(val component: StartsPopularComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
    }
}