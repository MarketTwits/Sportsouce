package com.markettwits.sportsouce.starts.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.settings.root.RootSettingsComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponent
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsScreenComponent
import kotlinx.serialization.Serializable

interface RootStartsComponent {

    val configStack: Value<ChildStack<Config, Child>>

    @Serializable
    sealed class Config {
        @Serializable
        data class Start(val startId: Int) : Config()

        @Serializable
        data object Starts : Config()

        @Serializable
        data object Search : Config()

        @Serializable
        data object Settings : Config()
    }


    sealed class Child {
        data class Start(val component: RootStartScreenComponent) : Child()
        data class Starts(val component: StartsScreenComponent) : Child()
        data class Search(val component: RootStartsSearchComponent) : Child()
        data class Settings(val component: RootSettingsComponent) : Child()
    }
}