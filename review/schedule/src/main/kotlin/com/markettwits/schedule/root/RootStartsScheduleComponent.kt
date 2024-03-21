package com.markettwits.schedule.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.schedule.schedule.presentation.component.StartsScheduleComponent
import com.markettwits.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootStartsScheduleComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object Schedule : Config
        @Serializable
        data class Start(val startId : Int) : Config
    }

    sealed interface Child {
        data class Schedule(val component : StartsScheduleComponent) : Child
        data class Start(val component : RootStartScreenComponent) : Child


    }
}