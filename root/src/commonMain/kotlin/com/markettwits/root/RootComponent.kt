package com.markettwits.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.bottom_bar.component.component.BottomBarComponent
import com.markettwits.root_profile.RootProfileComponent
import com.markettwits.starts.root.RootStartsComponentBase
import kotlinx.serialization.Serializable

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    val slotChild: Value<ChildSlot<*, Navigation>>

    @Serializable
    sealed class Configuration {
        @Serializable
        data object Starts : Configuration()
        @Serializable
        data object Review : Configuration()
        @Serializable
        data object Profile : Configuration()
    }

    sealed class Child {
        data class Starts(val component: RootStartsComponentBase) : Child()
        data class Profile(val component: RootProfileComponent) : Child()
        data class Review(val component : RootReviewComponent) : Child()
    }

    @Serializable
    sealed class SlotConfiguration {
        @Serializable
        data object BottomBar : SlotConfiguration()
    }

    sealed interface Navigation {
        data class BottomBar(val component: BottomBarComponent) : Navigation
    }
}