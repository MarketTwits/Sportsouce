package com.markettwits.sportsouce.starts.popular.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.starts.popular.presentation.component.StartsPopularComponent
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