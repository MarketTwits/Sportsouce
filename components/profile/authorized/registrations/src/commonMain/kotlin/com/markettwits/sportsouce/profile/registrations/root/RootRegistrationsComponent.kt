package com.markettwits.sportsouce.profile.registrations.root

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.profile.registrations.list.presentation.component.RegistrationsComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootRegistrationsComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

    @Serializable
    sealed class ConfigStack {
        @Serializable
        data object Registrations : ConfigStack()

        @Serializable
        data class Start(val startId: Int) : ConfigStack()
    }

    sealed class ChildStack {
        data class Registrations(val component: RegistrationsComponent) : ChildStack()
        data class Start(val component: RootStartScreenComponent) : ChildStack()
    }

}