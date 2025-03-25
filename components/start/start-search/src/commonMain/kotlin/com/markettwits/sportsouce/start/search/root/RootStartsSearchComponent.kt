package com.markettwits.sportsouce.start.search.root

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterComponent
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.search.presentation.component.StartsSearchComponent
import kotlinx.serialization.Serializable

interface RootStartsSearchComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    @Serializable
    sealed interface ConfigStack {
        @Serializable
        data object Search : ConfigStack

        @Serializable
        data class Start(val startId: Int) : ConfigStack

//        @Serializable
//        data object Filter : ConfigStack
    }

    sealed class ChildStack {
        data class Start(val component: RootStartScreenComponent) : ChildStack()

        //data class Filter(val component: RootStartFilterComponent) : ChildStack()
        data class Search(val component: StartsSearchComponent) : ChildStack()
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data class Filter(val filterUi: StartFilterUi?) : ConfigSlot
    }

    sealed class ChildSlot {
        data class Filter(val component: StartFilterComponent) : ChildSlot()
    }
}