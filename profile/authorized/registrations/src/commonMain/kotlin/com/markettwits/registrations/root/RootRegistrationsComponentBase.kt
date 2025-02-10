package com.markettwits.registrations.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.registrations.list.presentation.component.RegistrationsComponentBase
import com.markettwits.registrations.list.presentation.store.RegistrationsDataStoreFactory
import com.markettwits.registrations.root.di.userStartRegistrationModule
import com.markettwits.start.root.RootStartScreenComponentBase

class RootRegistrationsComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit
) : RootRegistrationsComponent, ComponentContext by context {
    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(userStartRegistrationModule)
    )

    private val stackNavigation = StackNavigation<RootRegistrationsComponent.ConfigStack>()


    override val childStack: Value<ChildStack<*, RootRegistrationsComponent.ChildStack>> =
        childStack(
            source = stackNavigation,
            serializer = RootRegistrationsComponent.ConfigStack.serializer(),
            handleBackButton = true,
            initialStack = { listOf(RootRegistrationsComponent.ConfigStack.Registrations) },
            childFactory = ::child,
        )

    private fun child(
        configStack: RootRegistrationsComponent.ConfigStack,
        componentContext: ComponentContext
    ): RootRegistrationsComponent.ChildStack {
        return when (configStack) {
            is RootRegistrationsComponent.ConfigStack.Start -> RootRegistrationsComponent.ChildStack.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = configStack.startId,
                    pop = stackNavigation::pop
                )
            )

            is RootRegistrationsComponent.ConfigStack.Registrations -> RootRegistrationsComponent.ChildStack.Registrations(
                RegistrationsComponentBase(
                    component = componentContext,
                    storeFactory = RegistrationsDataStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        dataSource = scope.get()
                    ),
                    pop = pop::invoke,
                    onItemClick = {},
                )
            )
        }
    }
}