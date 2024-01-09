package com.markettwits.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.start.root.RootStartScreenComponent
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.starts.data.BaseStartsDataSource
import com.markettwits.starts.data.StartsCloudToUiMapper
import kotlinx.serialization.Serializable
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.provider.HttpClientProvider
import ru.alexpanov.core_network.provider.JsonProvider

class DefaultStartsComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    private val _configStack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Starts,
            handleBackButton = true,
            childFactory = ::child,
        )

    val childStack: Value<ChildStack<*, Child>> get() = _configStack


    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): Child =
        when (config) {
            is Config.Start -> Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is Config.Starts ->
                Child.Starts(StartsScreenComponent(
                    componentContext = componentContext,
                    dataSource = BaseStartsDataSource(
                        StartsRemoteDataSourceImpl(
                            HttpClientProvider(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                        StartsCloudToUiMapper.Base(
                            BaseTimeMapper()
                        )
                    ),
                    toDetail = {
                        onItemClick(it)
                    }
                ))

        }


    @Serializable
    sealed class Config {
        @Serializable
        data class Start(
            val startId: Int,
        ) : Config()

        @Serializable
        data object Starts : Config()
    }

    sealed class Child {
        data class Start(val component: RootStartScreenComponent) : Child()
        data class Starts(val component: StartsScreenComponent) : Child()

    }

    fun onItemClick(startdId: Int) {
        navigation.push(Config.Start(startdId))
    }

}
