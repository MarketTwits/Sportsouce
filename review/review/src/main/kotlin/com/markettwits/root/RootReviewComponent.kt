package com.markettwits.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.random.root.presentation.RootStartRandomComponent
import com.markettwits.review.presentation.ReviewComponent
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start_filter.root.RootStartFilterComponent
import com.markettwits.start_filter.start_filter.presentation.StartFilterComponent
import kotlinx.serialization.Serializable

interface RootReviewComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object Review : Config
        @Serializable
        data class Start(val startId: Int) : Config
        @Serializable
        data object Filter : Config
        @Serializable
        data object Random : Config
    }

    sealed interface Child {
        data class Review(val component: ReviewComponent, val newsComponent : RootNewsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
        data class Filter(val component : RootStartFilterComponent) : Child
        data class Random(val component : RootStartRandomComponent) : Child
    }
}