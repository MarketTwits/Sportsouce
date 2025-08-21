package com.markettwits.sportsouce.profile.registrations.presentation.root

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import kotlinx.serialization.Serializable

interface RootRegistrationsComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

    @Serializable
    sealed class ConfigStack {
        @Serializable
        data object Registrations : ConfigStack()

        data class Registration(val order: StartOrderInfo) : ConfigStack()

        @Serializable
        data class Start(val startId: Int) : ConfigStack()
    }

    sealed class ChildStack {
        data class Registrations(val component: RegistrationsComponent) : ChildStack()
        data class Registration(val component: StartOrderComponent) : ChildStack()
        data class Start(val component: RootStartScreenComponent) : ChildStack()
    }

}