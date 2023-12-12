package com.markettwits.root.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.navigate
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.markettwits.start.StartScreenComponent
import com.markettwits.starts.BaseStartsDataSource
import com.markettwits.starts.DefaultStartsComponent
import com.markettwits.starts.data.BaseTimeMapper
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.StartsScreenComponent
import kotlinx.serialization.Serializable
import ru.alexpanov.core_network.api.SportSouceReposiotryImpl
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    private val stack =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialStack = { listOf(Configuration.Starts) },
            handleBackButton = true,
            childFactory = ::createChild,
        )

    fun navigate(configuration: Configuration) {
        navigation.bringToFront(configuration)
    }

    val childStack: Value<ChildStack<*, Child>> = stack
    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child =
        when (configuration) {
            is Configuration.Starts -> Child.Starts(
                DefaultStartsComponent(
                    componentContext = componentContext,
                )
            )

            is Configuration.Profile -> Child.Profile
        }

    @Serializable
    sealed class Configuration {
        @Serializable
        object Starts : Configuration()

        @Serializable
        object Profile : Configuration()
    }

    sealed class Child {

        data class Starts(val component: DefaultStartsComponent) : Child()
        object Profile : Child()
    }

}