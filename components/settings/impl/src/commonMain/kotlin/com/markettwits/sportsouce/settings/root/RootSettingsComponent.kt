package com.markettwits.sportsouce.settings.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponent
import com.markettwits.sportsouce.settings.internal.change_theme.component.ChangeThemeComponent
import com.markettwits.sportsouce.settings.internal.settings_menu.component.SettingsComponent
import kotlinx.serialization.Serializable

interface RootSettingsComponent {

    val childStack: Value<ChildStack<*, StackChild>>
    val childSlot: Value<ChildSlot<*, SlotChild>>

    @Serializable
    sealed class StackConfig {
        @Serializable
        data object SettingsMenu : StackConfig()

        @Serializable
        data object SelfUpdate : StackConfig()
    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data object ChangeTheme : SlotConfig
    }

    sealed class StackChild {
        data class SettingsMenu(val component: SettingsComponent) : StackChild()
        data class SelfUpdate(val component: SelfUpdateComponent) : StackChild()
    }

    sealed interface SlotChild {
        data class ChangeTheme(val component: ChangeThemeComponent) :
            SlotChild
    }
}