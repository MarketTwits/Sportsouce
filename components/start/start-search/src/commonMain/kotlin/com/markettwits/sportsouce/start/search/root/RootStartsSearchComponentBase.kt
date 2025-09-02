package com.markettwits.sportsouce.start.search.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterComponentBase
import com.markettwits.sportsouce.start.search.root.di.rootStartsSearchModule
import com.markettwits.sportsouce.start.search.search.presentation.component.StartsSearchComponentBase
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore


class RootStartsSearchComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit,
) : RootStartsSearchComponent,
    ComponentContext by context {
    private val stackNavigation = StackNavigation<RootStartsSearchComponent.ConfigStack>()
    private val slotNavigation = SlotNavigation<RootStartsSearchComponent.ConfigSlot>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootStartsSearchModule)
    )

    override val childStack: Value<ChildStack<*, RootStartsSearchComponent.ChildStack>> =
        childStack(
            source = stackNavigation,
            serializer = RootStartsSearchComponent.ConfigStack.serializer(),
            initialConfiguration = RootStartsSearchComponent.ConfigStack.Search,
        handleBackButton = true,
        childFactory = ::child,
    )
    override val childSlot: Value<ChildSlot<*, RootStartsSearchComponent.ChildSlot>> = childSlot(
        source = slotNavigation,
        serializer = RootStartsSearchComponent.ConfigSlot.serializer(),
        childFactory = ::childSlot
    )

    private fun child(
        config: RootStartsSearchComponent.ConfigStack,
        componentContext: ComponentContext,
    ): RootStartsSearchComponent.ChildStack =
        when (config) {
            is RootStartsSearchComponent.ConfigStack.Start -> RootStartsSearchComponent.ChildStack.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    input = StartScreenInput.Item(config.startItem),
                    pop = stackNavigation::pop
                )
            )

            is RootStartsSearchComponent.ConfigStack.Search -> RootStartsSearchComponent.ChildStack.Search(
                StartsSearchComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    back = pop::invoke,
                    filter = {
                        slotNavigation.activate(RootStartsSearchComponent.ConfigSlot.Filter(it))
                    },
                    start = {
                        stackNavigation.pushNew(RootStartsSearchComponent.ConfigStack.Start(it))
                    }
                )
            )
        }

    private fun childSlot(
        config: RootStartsSearchComponent.ConfigSlot,
        componentContext: ComponentContext,
    ): RootStartsSearchComponent.ChildSlot =
        when (config) {
            is RootStartsSearchComponent.ConfigSlot.Filter -> RootStartsSearchComponent.ChildSlot.Filter(
                StartFilterComponentBase(
                    context = componentContext,
                    filterUi = config.filterUi,
                    show = { filter, sorted ->
                        slotNavigation.dismiss {
                            (childStack.value.active.instance as? RootStartsSearchComponent.ChildStack.Search)?.component?.obtainEvent(
                                StartsSearchStore.Intent.OnFilterApply(filter, sorted)
                            )
                        }
                    },
                    storeFactory = scope.get(),
                    pop = slotNavigation::dismiss
                )
            )
        }
}