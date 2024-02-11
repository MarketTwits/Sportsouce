package com.markettwits.start_search.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start_search.search.presentation.component.StartsSearchComponent
import kotlinx.serialization.Serializable

interface RootStartsSearchComponent {
    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {
        @Serializable
        data object Search : Config

        @Serializable
        data class Start(val startId: Int) : Config
//        @Serializable
//        data object Filter : Config
    }

    sealed interface Child {
        data class Start(val component: RootStartScreenComponent) : Child

        // data class Filter(val component : RootStartFilterComponent) : Child
        data class Search(val component: StartsSearchComponent) : Child

    }
}