package com.markettwits.start_filter.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start_filter.start_filter.presentation.StartFilterComponent
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.start_filter.starts.StartsFilteredComponent
import kotlinx.serialization.Serializable

interface RootStartFilterComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object Filter : Config
        @Serializable
        data class Starts(val request: StartFilterUi) : Config

        @Serializable
        data class Start(val startId : Int) : Config
    }

    sealed interface Child {
        data class Starts(val component: StartsFilteredComponent) : Child
        data class Start(val component : RootStartScreenComponent) : Child
        data class Filter(val component : StartFilterComponent) : Child
    }
}