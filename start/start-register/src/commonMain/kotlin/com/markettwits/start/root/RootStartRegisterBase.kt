package com.markettwits.start.root

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
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponentBase
import com.markettwits.start.register.presentation.order.presentation.component.OrderComponentComponentBase
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponentBase
import com.markettwits.start.register.presentation.success.RegisterSuccessComponentBase

/**
 * @param pop callback for navigate to back
 * @param content : first - startId, second - DistanceItem, third -
 *     paymentDisabled, fourth - paymentType
 */

class RootStartRegisterBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val content: RootStartRegister.StartRegisterParams
) : ComponentContext by componentContext, RootStartRegister {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext(false)
    }
    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val stackNavigation = StackNavigation<RootStartRegister.ConfigStack>()
    private val slotNavigation = SlotNavigation<RootStartRegister.ConfigSlot>()
    override val childStack: Value<ChildStack<*, RootStartRegister.ChildStack>> = childStack(
        source = stackNavigation,
        serializer = RootStartRegister.ConfigStack.serializer(),
        initialConfiguration = RootStartRegister.ConfigStack.StartRegistration(
            startId = content.startId,
            distanceInfo = content.distanceItem,
            paymentDisabled = content.isPaymentDisabled,
            paymentType = content.paymentType,
            startTitle = content.startTitle,
            discounts = content.discounts
        ),
        handleBackButton = true,
        childFactory = ::childStack,
    )
    override val childSlot: Value<ChildSlot<*, RootStartRegister.ChildSlot>> = childSlot(
        serializer = RootStartRegister.ConfigSlot.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::childSlot
    )

    private fun childSlot(
        config: RootStartRegister.ConfigSlot,
        componentContext: ComponentContext
    ): RootStartRegister.ChildSlot = when (config) {
        is RootStartRegister.ConfigSlot.StartPromo -> RootStartRegister.ChildSlot.StartPromo(
            RegistrationPromoComponentBase(
                componentContext = componentContext,
                startId = config.startId,
                promo = config.promo,
                storeFactory = scope.get(),
                applyPromo = { promo, percent ->
                    slotNavigation.dismiss()
                    (childStack.value.active.instance
                            as? RootStartRegister.ChildStack.StartOrder)?.component?.obtainEvent(
                        OrderStore.Intent.ApplyPromo(promo, percent)
                    )
                },
                dismiss = { slotNavigation.dismiss() }
            )
        )
    }

    private fun childStack(
        config: RootStartRegister.ConfigStack,
        componentContext: ComponentContext,
    ): RootStartRegister.ChildStack =
        when (config) {
            is RootStartRegister.ConfigStack.StartRegistration ->
                RootStartRegister.ChildStack.StartOrder(
                    OrderComponentComponentBase(
                        componentContext = componentContext,
                        startId = config.startId,
                        paymentDisabled = config.paymentDisabled,
                        distanceInfo = config.distanceInfo,
                        discounts = config.discounts,
                        paymentType = config.paymentType,
                        startTitle = config.startTitle,
                        storeFactory = scope.get(),
                        pop = pop::invoke,
                        onClickPromo = { startId, promo ->
                            slotNavigation.activate(
                                RootStartRegister.ConfigSlot.StartPromo(
                                    startId,
                                    promo
                                )
                            )
                        },
                        onClickMember = { member, id, members ->
                            stackNavigation.push(
                                RootStartRegister.ConfigStack.StartRegistrationMember(
                                    id,
                                    members,
                                    member,
                                )
                            )
                        },
                        openSuccess = {
                            stackNavigation.replaceCurrent(RootStartRegister.ConfigStack.StartRegistrationSuccess)
                        }
                    )
                )

            is RootStartRegister.ConfigStack.StartRegistrationMember -> RootStartRegister.ChildStack.StartRegistrationMember(
                RegistrationMemberComponentBase(
                    componentContext = componentContext,
                    startStatement = config.startStatement,
                    memberId = config.memberId,
                    storeFactory = scope.get(),
                    pop = stackNavigation::pop,
                    membersProfile = config.profileMembers,
                    apply = { member, id ->
                        stackNavigation.pop {
                            (childStack.value.active.instance
                                    as? RootStartRegister.ChildStack.StartOrder)?.component?.obtainEvent(
                                OrderStore.Intent.UpdateMember(member, id)
                            )
                        }
                    }
                )
            )

            RootStartRegister.ConfigStack.StartRegistrationSuccess -> RootStartRegister.ChildStack.StartRegistrationSuccess(
                RegisterSuccessComponentBase(
                    componentComponent = componentContext,
                    next = pop::invoke
                )
            )
        }
}