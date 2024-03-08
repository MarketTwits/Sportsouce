package com.markettwits.random.root.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.random.random.presentation.StartRandomComponentBase
import com.markettwits.random.random.presentation.store.StartRandomStoreFactory
import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.random.root.di.createStartRandomModule
import com.markettwits.start.root.RootStartScreenComponentBase

class RootStartRandomComponentBase(
    context: ComponentContext,
    private val dependencies: StartRandomComponentDependencies,
    private val pop: () -> Unit
) : RootStartRandomComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartRandomComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        createStartRandomModule(dependencies)
    )

    override val childStack: Value<ChildStack<*, RootStartRandomComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartRandomComponent.Config.serializer(),
        initialConfiguration = RootStartRandomComponent.Config.Random,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartRandomComponent.Config,
        componentContext: ComponentContext,
    ): RootStartRandomComponent.Child =
        when (config) {
            is RootStartRandomComponent.Config.Random -> RootStartRandomComponent.Child.Random(
                StartRandomComponentBase(
                    context = componentContext,
                    storeFactory = StartRandomStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        repository = scope.get(),
                    ),
                    openStart = {
                        navigation.replaceAll(RootStartRandomComponent.Config.Start(it))
                    },
                    pop = navigation::pop
                )
            )

            is RootStartRandomComponent.Config.Start -> RootStartRandomComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = pop::invoke
                )
            )
        }
}