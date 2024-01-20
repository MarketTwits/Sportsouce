package com.markettwits.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.news_event.NewsEventComponent
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.news_list.presentation.NewsComponent
import com.markettwits.popular.popular.presentation.PopularStartsComponent
import com.markettwits.random.root.presentation.RootStartRandomComponent
import com.markettwits.review.presentation.ReviewComponent
import com.markettwits.schedule.root.RootStartsScheduleComponent
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
        @Serializable
        data object Schedule : Config
        @Serializable
        data object Popular : Config
        @Serializable
        data class NewsEvent(val news : NewsInfo) : Config
    }

    sealed interface Child {
        data class Review(val component: ReviewComponent, val newsComponent : NewsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
        data class Filter(val component : RootStartFilterComponent) : Child
        data class Random(val component : RootStartRandomComponent) : Child
        data class Schedule(val component : RootStartsScheduleComponent) : Child
        data class Popular(val component : PopularStartsComponent) : Child
        data class NewsEvent(val component : NewsEventComponent) : Child
    }
}