package com.markettwits.registrations.root_registrations

import com.arkivanov.decompose.value.Value
import com.markettwits.registrations.registrations.presentation.component.RegistrationsComponent
import com.markettwits.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootRegistrationsComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

    fun dismissSlotChild()

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