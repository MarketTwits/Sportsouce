package com.markettwits.schedule.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.schedule.schedule.presentation.component.StartsScheduleComponent
import com.markettwits.schedule.schedule.presentation.components.detail.component.StartDetailScheduleComponent
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.serialization.Serializable

interface RootStartsScheduleComponent {
    val childStack: Value<ChildStack<*, Child>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    @Serializable
    sealed interface Config {
        @Serializable
        data object Schedule : Config

        @Serializable
        data class Start(val startId: Int) : Config
    }

    sealed interface Child {
        data class Schedule(val component: StartsScheduleComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
    }

    sealed interface ChildSlot {
        data class ScheduleDetail(val component: StartDetailScheduleComponent) : ChildSlot
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data class ScheduleDetail(val start: List<StartsListItem>) : ConfigSlot
    }
}