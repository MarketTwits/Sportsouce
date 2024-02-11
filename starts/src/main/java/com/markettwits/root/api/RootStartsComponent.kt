package com.markettwits.root.api

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start_search.root.RootStartsSearchComponent
import com.markettwits.starts.presentation.StartsScreenComponent
import kotlinx.serialization.Serializable

interface RootStartsComponent {
    val configStack: Value<ChildStack<Config, Child>>

    @Serializable
    sealed class Config {
        @Serializable
        data class Start(
            val startId: Int,
        ) : Config()

        @Serializable
        data object Starts : Config()

        @Serializable
        data object Search : Config()
    }

    sealed class Child {
        data class Start(val component: RootStartScreenComponent) : Child()
        data class Starts(val component: StartsScreenComponent) : Child()
        data class Search(val component: RootStartsSearchComponent) : Child()
    }
    sealed interface LaunchPolicy{
        data object Main : LaunchPolicy
        data class WithFilter(val request : Map<String,String>) : LaunchPolicy
    }
}