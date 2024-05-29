package com.markettwits.starts.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.settings.root.RootSettingsComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_search.root.RootStartsSearchComponentBase
import com.markettwits.starts.starts.di.startsModule
import com.markettwits.starts.starts.presentation.component.StartsScreenComponent

class RootStartsComponentBase(
    private val componentContext: ComponentContext
) : RootStartsComponent, ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startsModule)
    )

    private val navigation = StackNavigation<RootStartsComponent.Config>()

    override val configStack = childStack(
        source = navigation,
        serializer = RootStartsComponent.Config.serializer(),
        initialConfiguration = RootStartsComponent.Config.Starts,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartsComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsComponent.Child =
        when (config) {
            is RootStartsComponent.Config.Start -> RootStartsComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Starts ->
                RootStartsComponent.Child.Starts(
                    component = StartsScreenComponent(
                        componentContext = componentContext,
                        dataSource = scope.get(),
                        toDetail = {
                            navigation.push(RootStartsComponent.Config.Start(it))
                        },
                        toSearch = {
                            navigation.push(RootStartsComponent.Config.Search)
                        },
                        toSettings = {
                            navigation.push(RootStartsComponent.Config.Settings)
                        }
                    ),
                )

            is RootStartsComponent.Config.Search -> RootStartsComponent.Child.Search(
                RootStartsSearchComponentBase(
                    context = componentContext,
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Settings -> RootStartsComponent.Child.Settings(
                RootSettingsComponentBase(
                    componentContext = componentContext,
                    pop = navigation::pop
                )
            )
        }
}
