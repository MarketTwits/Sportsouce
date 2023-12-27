package com.markettwits.start.presentation.membres.filter_screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.presentation.membres.filter_screen.component.StartMembersSortItemRow
import com.markettwits.start.presentation.membres.list.StartMembersScreenInstanceKeeper
import com.markettwits.start.presentation.membres.list.StartMembersUi
import kotlinx.serialization.builtins.ListSerializer

class StartMembersFilterScreenComponent(
    private val context: ComponentContext,
    private val back: () -> Unit,
    //private val items: List<StartMembersUi>,
    private val items : List<MembersFilterGroup>,
    private val apply : (List<MembersFilterGroup>) -> Unit
) : StartMembersFilterScreen, ComponentContext by context {

    override val filter: MutableValue<List<MembersFilterGroup>> = MutableValue(
        stateKeeper.consume(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) ?: items
    )

    init {
      //  updateFilterGroups()
        stateKeeper.register(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) {filter.value }
    }


    override fun goBack() {
        back()
    }

    override fun reset() {

    }

    override fun apply() {
        apply(filter.value)
    }

    override fun selectItem(item: MembersFilterItem) {

    }
    override fun toggleFilterItemState(categoryIndex: Int, itemIndex: Int) {
        val currentFilterGroups = filter.value.orEmpty().toMutableList()
        val category = currentFilterGroups[categoryIndex]

        // Toggle the state of the specified item
        val toggledItems = category.items.mapIndexed { index, filterItem ->
            if (index == itemIndex) {
                when (filterItem) {
                    is MembersFilterItem.Base -> MembersFilterItem.Selected(filterItem.title)
                    is MembersFilterItem.Selected -> MembersFilterItem.Base(filterItem.title)
                }
            } else {
                filterItem
            }
        }

        // Update the category with the toggled items
        currentFilterGroups[categoryIndex] = category.copy(items = toggledItems)

        // Notify observers about the updated data
        filter.value = currentFilterGroups
    }

}