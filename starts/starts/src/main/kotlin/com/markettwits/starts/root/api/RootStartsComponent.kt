package com.markettwits.starts.root.api

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.inappnotification.api.InAppNotificationRenderer
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponent
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponent
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start_search.root.RootStartsSearchComponent
import com.markettwits.starts.starts.presentation.component.StartsScreenComponent
import kotlinx.serialization.Serializable

interface RootStartsComponent {
    val configStack: Value<ChildStack<Config, Child>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<ConfigSlot, ChildSlot>>

    @Serializable
    sealed class Config {
        @Serializable
        data class Start(val startId: Int) : Config()

        @Serializable
        data object Starts : Config()
        @Serializable
        data object Search : Config()

        @Serializable
        data class Notification(val newAppVersion: NewAppVersion) : Config()
    }


    sealed class Child {
        data class Start(val component: RootStartScreenComponent) : Child()
        data class Starts(val component: StartsScreenComponent) : Child()
        data class Search(val component: RootStartsSearchComponent) : Child()
        data class Notification(val component: SelfUpdateComponent) : Child()
    }

    @Serializable
    sealed class ConfigSlot {
        @Serializable
        data object Notification : ConfigSlot()
    }

    sealed class ChildSlot {
        data class Notification(
            val component: InAppNotificationComponent,
            val render: InAppNotificationRenderer
        ) : ChildSlot()
    }

}