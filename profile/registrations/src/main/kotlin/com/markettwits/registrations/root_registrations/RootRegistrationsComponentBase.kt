package com.markettwits.registrations.root_registrations

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
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.registrations.paymant_dialog.RegistrationsPaymentComponentBase
import com.markettwits.registrations.registrations.presentation.RegistrationsComponentBase
import com.markettwits.registrations.registrations.presentation.RegistrationsDataStoreFactory
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.registrations.root_registrations.di.userStartRegistrationModule

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
    private val slotNavigation = SlotNavigation<RootRegistrationsComponent.ConfigChild>()

    override val childStack: Value<ChildStack<*, RootRegistrationsComponent.ChildStack>> =
        childStack(
            source = stackNavigation,
            serializer = RootRegistrationsComponent.ConfigStack.serializer(),
            handleBackButton = true,
            initialStack = { listOf(RootRegistrationsComponent.ConfigStack.Registrations) },
            childFactory = ::child,
        )
    override val childSlot: Value<ChildSlot<*, RootRegistrationsComponent.ChildSlot>> = childSlot(
        serializer = RootRegistrationsComponent.ConfigChild.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::child
    )

    override fun dismissSlotChild() {
        slotNavigation.dismiss()
    }

    private fun child(
        configStack: RootRegistrationsComponent.ConfigChild,
        componentContext: ComponentContext
    ): RootRegistrationsComponent.ChildSlot {
        return when (configStack) {
            is RootRegistrationsComponent.ConfigChild.PaymentDialog ->
                RootRegistrationsComponent.ChildSlot.PaymentDialog(
                    component = (RegistrationsPaymentComponentBase(
                        paymentItems = configStack.paymentState,
                        context = componentContext,
                        storeFactory = scope.get()
                    ))
                )
        }

    }

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
                    onItemClick = {
                        stackNavigation.push(RootRegistrationsComponent.ConfigStack.Start(it))
                    },
                    showPaymentDialog = {
                        slotNavigation.activate(
                            RootRegistrationsComponent.ConfigChild.PaymentDialog(it)
                        )
                    }
                )
            )
        }
    }
}