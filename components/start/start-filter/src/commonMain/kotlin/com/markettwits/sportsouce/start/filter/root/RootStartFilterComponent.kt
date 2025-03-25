package com.markettwits.sportsouce.start.filter.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilter
import com.markettwits.sportsouce.start.filter.start_filter.presentation.component.StartFilterComponent
import com.markettwits.sportsouce.start.filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.filter.starts.StartsFilteredComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartFilterComponent {
    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {

        @Serializable
        data object Filter : Config

        @Serializable
        data class Starts(val request: StartFilterUi, val sorted: StartFilter.Sorted) : Config

        @Serializable
        data class Start(val startId: Int) : Config
    }

    sealed interface Child {
        data class Starts(val component: StartsFilteredComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
        data class Filter(val component: StartFilterComponent) : Child
    }
}