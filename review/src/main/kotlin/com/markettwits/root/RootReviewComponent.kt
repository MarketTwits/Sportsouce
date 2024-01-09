package com.markettwits.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.review.presentation.ReviewComponent
import com.markettwits.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootReviewComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object Review : Config
        @Serializable
        data class Start(val startId: Int) : Config
    }

    sealed interface Child {
        data class Review(val component: ReviewComponent, val newsComponent : RootNewsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
    }
}