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
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.start.di.startModule
import com.markettwits.start.di.startRegistrationModule
import com.markettwits.start.presentation.comments.comments.StartCommentsComponentBase
import com.markettwits.start.presentation.comments.comments.StartCommentsStoreFactory
import com.markettwits.start.presentation.member.component.RegistrationMemberComponentBase
import com.markettwits.start.presentation.membres.filter_screen.HandleMembersFilterBase
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersScreenComponent
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.membres.list.filter.MembersFilterBase
import com.markettwits.start.presentation.order.presentation.store.OrderStore
import com.markettwits.start.presentation.promo.component.RegistrationPromoComponentBase
import com.markettwits.start.presentation.start.component.StartScreenComponentComponentBase

class RootStartScreenComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val pop: () -> Unit
) : RootStartScreenComponent,
    ComponentContext by context {
    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext(false)
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startModule, startRegistrationModule)
    )
    private val navigation = StackNavigation<RootStartScreenComponent.Config>()
    private val slotNavigation = SlotNavigation<RootStartScreenComponent.ConfigChild>()

    override val childSlot: Value<ChildSlot<*, RootStartScreenComponent.ChildSlot>> = childSlot(
        serializer = RootStartScreenComponent.ConfigChild.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::childSlot
    )

    override val childStack: Value<ChildStack<*, RootStartScreenComponent.Child>> =
        childStack(
            source = navigation,
            serializer = RootStartScreenComponent.Config.serializer(),
            initialConfiguration = RootStartScreenComponent.Config.Start(startId, true),
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun childSlot(
        config: RootStartScreenComponent.ConfigChild,
        componentContext: ComponentContext
    ): RootStartScreenComponent.ChildSlot = when (config) {
        is RootStartScreenComponent.ConfigChild.StartPromo -> RootStartScreenComponent.ChildSlot.StartPromo(
            RegistrationPromoComponentBase(
                componentContext = componentContext,
                startId = config.startId,
                promo = config.promo,
                storeFactory = scope.get(),
                applyPromo = { promo, percent ->
                    slotNavigation.dismiss()
                    (childStack.value.active.instance
                            as? RootStartScreenComponent.Child.StartOrder)?.component?.obtainEvent(
                        OrderStore.Intent.ApplyPromo(promo, percent)
                    )
                },
                dismiss = { slotNavigation.dismiss() }
            )
        )
    }

    private fun child(
        config: RootStartScreenComponent.Config,
        componentContext: ComponentContext,
    ): RootStartScreenComponent.Child =
        when (config) {
            is RootStartScreenComponent.Config.Start -> RootStartScreenComponent.Child.Start(
                StartScreenComponentComponentBase(
                    componentContext = componentContext,
                    startId = config.startId,
                    back = pop::invoke,
                    register = { distanceInfo, paymentDisabled, paymentType ->
                        navigation.push(
                            RootStartScreenComponent.Config.StartRegistration(
                                startId = startId,
                                distanceInfo = distanceInfo,
                                paymentDisabled = paymentDisabled,
                                paymentType = paymentType
                            )
                        )
                    },
                    storeFactory = scope.get(),
                    members = { id: Int, list: List<StartMembersUi> ->
                        openMembersScreen(startId = id, items = list, emptyList())
                    }
                ),
                StartCommentsComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    storeFactory = StartCommentsStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        service = scope.get(),
                    )
                )
            )

            is RootStartScreenComponent.Config.StartMembers -> RootStartScreenComponent.Child.StartMembers(
                StartMembersScreenComponent(
                    componentContext = componentContext,
                    membersUi = config.items,
                    openFilterScreen = ::openMembersFilter,
                    onBack = navigation::pop,
                    membersFilter = MembersFilterBase()
                ),
            )

            is RootStartScreenComponent.Config.StartMembersFilter -> RootStartScreenComponent.Child.StartMembersFilter(
                StartMembersFilterScreenComponent(
                    context = componentContext,
                    items = config.items,
                    handleMembersFilter = HandleMembersFilterBase(),
                    apply = { filter ->
                        navigation.pop { // Pop ItemDetailsComponent
                            (childStack.value.active.instance as? RootStartScreenComponent.Child.StartMembers)?.component?.updateFilter(
                                filter = filter
                            )
                        }
                    },
                    back = navigation::pop
                )
            )

            is RootStartScreenComponent.Config.StartRegistration ->
                RootStartScreenComponent.Child.StartOrder(
                    com.markettwits.start.presentation.order.presentation.component.OrderComponentComponentBase(
                        componentContext = componentContext,
                        startId = config.startId,
                        paymentDisabled = config.paymentDisabled,
                        distanceInfo = config.distanceInfo,
                        paymentType = config.paymentType,
                        storeFactory = scope.get(),
                        pop = navigation::pop,
                        onClickPromo = { startId, promo ->
                            slotNavigation.activate(
                                RootStartScreenComponent.ConfigChild.StartPromo(
                                    startId,
                                    promo
                                )
                            )
                        },
                        onClickMember = { member, id ->
                            navigation.push(
                                RootStartScreenComponent.Config.StartRegistrationMember(
                                    id,
                                    member
                                )
                            )
                        }
                    )
                )

            is RootStartScreenComponent.Config.StartRegistrationMember -> RootStartScreenComponent.Child.StartRegistrationMember(
                RegistrationMemberComponentBase(
                    componentContext = componentContext,
                    startStatement = config.startStatement,
                    memberId = config.memberId,
                    storeFactory = scope.get(),
                    pop = navigation::pop,
                    apply = { member, id ->
                        navigation.pop {
                            (childStack.value.active.instance
                                    as? RootStartScreenComponent.Child.StartOrder)?.component?.obtainEvent(
                                OrderStore.Intent.UpdateMember(member, id)
                            )
                        }
                    }
                )
            )
        }

    private fun openMembersScreen(
        startId: Int,
        items: List<StartMembersUi>,
        filter: List<MembersFilterGroup>
    ) {
        navigation.push(RootStartScreenComponent.Config.StartMembers(startId, items, filter))
    }

    private fun openMembersFilter(items: List<MembersFilterGroup>) {
        navigation.push(RootStartScreenComponent.Config.StartMembersFilter(items))
    }
}