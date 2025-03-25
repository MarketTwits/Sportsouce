package com.markettwits.sportsouce.starts.random.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.starts.random.presentation.StartRandomComponent
import kotlinx.serialization.Serializable

interface RootStartRandomComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object Random : Config
        @Serializable
        data class Start(val startId : Int) : Config
    }

    sealed interface Child {
        data class Random(val component : StartRandomComponent) : Child
        data class Start(val component : RootStartScreenComponent) : Child
    }
}