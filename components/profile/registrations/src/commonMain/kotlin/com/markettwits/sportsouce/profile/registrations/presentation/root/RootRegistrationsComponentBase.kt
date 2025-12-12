package com.markettwits.sportsouce.profile.registrations.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponentBase
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsDataStoreFactory
import com.markettwits.sportsouce.profile.registrations.presentation.root.RootRegistrationsComponent.ChildStack.*
import com.markettwits.sportsouce.profile.registrations.presentation.root.di.userStartRegistrationModule
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput.Id
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase

class RootRegistrationsComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit,
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
        componentContext: ComponentContext,
    ): RootRegistrationsComponent.ChildStack {
        return when (configStack) {
            is RootRegistrationsComponent.ConfigStack.Start -> Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    input = if (configStack.orderId == null)
                        Id(
                            configStack.startId
                        ) else StartScreenInput.ReReg(configStack.orderId, configStack.startId),
                    pop = stackNavigation::pop
                )
            )

            is RootRegistrationsComponent.ConfigStack.Registrations -> Registrations(
                component = RegistrationsComponentBase(
                    component = componentContext,
                    storeFactory = RegistrationsDataStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        dataSource = scope.get(),
                        exceptionTracker = scope.get()
                    ),
                    pop = pop::invoke,
                    onItemClick = {
                        stackNavigation.pushNew(RootRegistrationsComponent.ConfigStack.Registration(it))
                    },
                )
            )

            is RootRegistrationsComponent.ConfigStack.Registration -> Registration(
                component = StartOrderComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    start = configStack.order,
                    dismiss = stackNavigation::pop,
                    openStart = {
                        stackNavigation.pushNew(RootRegistrationsComponent.ConfigStack.Start(it))
                    },
                    onReRegistration = { startId, orderId ->
                        stackNavigation.pushNew(RootRegistrationsComponent.ConfigStack.Start(startId, orderId))
                    }
                )
            )
        }
    }
}