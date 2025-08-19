package com.markettwits.sportsouce.starts.popular.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.starts.popular.di.popularStartsModule
import com.markettwits.sportsouce.starts.popular.presentation.component.StartsPopularComponentBase

class RootStartsPopularComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit
) : RootStartsPopularComponent, ComponentContext by context {
    private val navigation = StackNavigation<RootStartsPopularComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(popularStartsModule)
    )

    override val childStack: Value<ChildStack<*, RootStartsPopularComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartsPopularComponent.Config.serializer(),
        initialConfiguration = RootStartsPopularComponent.Config.Popular,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun createStartScreenInput(startId: String): com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput {
        // Check if startId is numeric or slug
        val numericId = startId.toIntOrNull()

        return if (numericId != null) {
            // startId is numeric, use as ID
            com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput.Id(numericId)
        } else {
            // startId is not numeric, treat as slug
            com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput.Slug(startId)
        }
    }

    private fun child(
        config: RootStartsPopularComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsPopularComponent.Child =
        when (config) {
            is RootStartsPopularComponent.Config.Popular -> RootStartsPopularComponent.Child.Popular(
                StartsPopularComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    pop = pop,
                    start = {
                        navigation.pushNew(RootStartsPopularComponent.Config.Start(it.toString()))
                    },
                )
            )

            is RootStartsPopularComponent.Config.Start -> RootStartsPopularComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    input = createStartScreenInput(config.startId),
                    pop = navigation::pop
                )
            )
        }
}