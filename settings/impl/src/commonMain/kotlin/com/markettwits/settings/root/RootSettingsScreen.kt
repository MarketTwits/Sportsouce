package com.markettwits.settings.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.selfupdater.components.selft_update.screen.SelfUpdateScreen
import com.markettwits.settings.internal.appearance.screen.AppearanceScreen
import com.markettwits.settings.internal.change_theme.components.ChangeThemeScreen
import com.markettwits.settings.internal.settings_menu.screen.SettingsScreen

@Composable
fun RootSettingsScreen(component: RootSettingsComponent) {
    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootSettingsComponent.SlotChild.ChangeTheme -> ChangeThemeScreen(
                component = child.component
            )

            is RootSettingsComponent.SlotChild.Appearance -> AppearanceScreen(component = child.component)
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootSettingsComponent.StackChild.SettingsMenu -> SettingsScreen(component = child.component)
            is RootSettingsComponent.StackChild.SelfUpdate -> SelfUpdateScreen(component = child.component)
        }
    }
}