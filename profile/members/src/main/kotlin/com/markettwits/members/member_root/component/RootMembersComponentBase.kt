package com.markettwits.members.member_root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.slot.navigate
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.members.member_detail.presentation.component.MemberDetailComponentBase
import com.markettwits.members.member_detail.presentation.store.MemberDetailStoreFactory
import com.markettwits.members.member_edit.presentation.component.MemberEditComponentBase
import com.markettwits.members.member_root.di.rootMembersModule
import com.markettwits.members.members_list.presentation.component.MembersListComponentBase
import com.markettwits.members.members_list.presentation.store.store.MembersListStore

class RootMembersComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit
) : ComponentContext by componentContext, RootMembersComponent {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootMembersModule)
    )

    private val stackNavigation = StackNavigation<RootMembersComponent.ConfigStack>()
    private val slotNavigation = SlotNavigation<RootMembersComponent.ConfigSlot>()

    override val childStack: Value<ChildStack<*, RootMembersComponent.ChildStack>> = childStack(
        source = stackNavigation,
        serializer = RootMembersComponent.ConfigStack.serializer(),
        initialConfiguration = RootMembersComponent.ConfigStack.MembersList,
        handleBackButton = true,
        childFactory = ::childStack,
    )
    override val childSlot: Value<ChildSlot<*, RootMembersComponent.ChildSlot>> = childSlot(
        source = slotNavigation,
        serializer = RootMembersComponent.ConfigSlot.serializer(),
        handleBackButton = true,
        childFactory = ::childSlot
    )


    private fun childStack(
        config: RootMembersComponent.ConfigStack,
        componentContext: ComponentContext,
    ): RootMembersComponent.ChildStack =
        when (config) {
            is RootMembersComponent.ConfigStack.MembersList -> RootMembersComponent.ChildStack.MembersList(
                MembersListComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    pop = pop::invoke,
                    goDetail = {
                        slotNavigation.activate(RootMembersComponent.ConfigSlot.MemberDetail(it))
                    },
                    addMember = {
                        slotNavigation.activate(RootMembersComponent.ConfigSlot.MemberEdit(null))
                    }
                ))
        }

    private fun childSlot(
        config: RootMembersComponent.ConfigSlot,
        componentContext: ComponentContext,
    ): RootMembersComponent.ChildSlot =
        when (config) {
            is RootMembersComponent.ConfigSlot.MemberDetail -> RootMembersComponent.ChildSlot.MemberDetail(
                MemberDetailComponentBase(
                    componentContext = componentContext,
                    storeFactory = MemberDetailStoreFactory(DefaultStoreFactory()),
                    member = config.member,
                    dismiss = slotNavigation::dismiss,
                    onClickEdit = {
                        slotNavigation.activate(RootMembersComponent.ConfigSlot.MemberEdit(it))
                    }
                )
            )

            is RootMembersComponent.ConfigSlot.MemberEdit -> RootMembersComponent.ChildSlot.MemberEdit(
                MemberEditComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    profileMember = config.member,
                    pop = {
                        config.member?.let {
                            RootMembersComponent.ConfigSlot.MemberDetail(
                                it
                            )
                        }?.let { slotNavigation.activate(it) }
                    },
                    apply = { member ->
                        slotNavigation.dismiss {
                            (childStack.value.active.instance
                                    as? RootMembersComponent.ChildStack.MembersList)?.component?.obtainEvent(
                                MembersListStore.Intent.Retry
                            )
                        }
                        slotNavigation.activate(RootMembersComponent.ConfigSlot.MemberDetail(member))
                    }
                )
            )
        }
}