package com.markettwits.settings.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponentBase
import com.markettwits.selfupdater.components.selft_update.di.selfUpdateComponentModule
import com.markettwits.settings.internal.appearance.component.AppearanceComponentBase
import com.markettwits.settings.internal.change_theme.component.ChangeThemeComponentBase
import com.markettwits.settings.internal.change_theme.di.changeThemeModule
import com.markettwits.settings.internal.settings_menu.component.HandleSettingsMenu
import com.markettwits.settings.internal.settings_menu.component.SettingsComponentBase
import com.markettwits.settings.internal.settings_menu.di.settingsMenuModule

class RootSettingsComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit
) : RootSettingsComponent,
    HandleSettingsMenu,
    ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(settingsMenuModule, changeThemeModule, selfUpdateComponentModule)
    )

    private val stackNavigation = StackNavigation<RootSettingsComponent.StackConfig>()

    private val slotNavigation = SlotNavigation<RootSettingsComponent.SlotConfig>()

    override val childSlot: Value<ChildSlot<*, RootSettingsComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            serializer = RootSettingsComponent.SlotConfig.serializer(),
            handleBackButton = true,
            childFactory = ::childSlot
        )


    override val childStack: Value<ChildStack<*, RootSettingsComponent.StackChild>> =
        childStack(
            source = stackNavigation,
            serializer = RootSettingsComponent.StackConfig.serializer(),
            initialConfiguration = RootSettingsComponent.StackConfig.SettingsMenu,
            handleBackButton = true,
            childFactory = ::childStack,
        )

    private fun childStack(
        config: RootSettingsComponent.StackConfig,
        componentContext: ComponentContext
    ): RootSettingsComponent.StackChild =
        when (config) {
            RootSettingsComponent.StackConfig.SettingsMenu -> RootSettingsComponent.StackChild.SettingsMenu(
                SettingsComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = pop::invoke,
                    output = { it.handleOutput(this) }
                )
            )

            RootSettingsComponent.StackConfig.SelfUpdate -> RootSettingsComponent.StackChild.SelfUpdate(
                SelfUpdateComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    newAppVersion = null,
                    goBack = stackNavigation::pop
                )
            )
        }

    private fun childSlot(
        config: RootSettingsComponent.SlotConfig,
        componentContext: ComponentContext
    ): RootSettingsComponent.SlotChild =
        when (config) {
            RootSettingsComponent.SlotConfig.ChangeTheme -> RootSettingsComponent.SlotChild.ChangeTheme(
                ChangeThemeComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = slotNavigation::dismiss
                )
            )

            is RootSettingsComponent.SlotConfig.Appearance -> RootSettingsComponent.SlotChild.Appearance(
                AppearanceComponentBase(
                    componentContext = componentContext,
                    pop = slotNavigation::dismiss,
                    storeFactory = scope.get()
                )
            )
        }

    override fun openChangeThemeScreen() {
        slotNavigation.activate(RootSettingsComponent.SlotConfig.ChangeTheme)
    }

    override fun openCheckUpdatesScreen() {
        stackNavigation.push(RootSettingsComponent.StackConfig.SelfUpdate)
    }

    override fun openAppearanceContent() {
        slotNavigation.activate(RootSettingsComponent.SlotConfig.Appearance)
    }
}