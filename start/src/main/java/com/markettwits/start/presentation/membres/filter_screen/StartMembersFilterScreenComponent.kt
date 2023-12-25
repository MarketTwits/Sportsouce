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

class StartMembersFilterScreenComponent(
    private val context: ComponentContext,
    private val back: () -> Unit,
    private val items: List<StartMembersUi>
) : StartMembersFilterScreen, ComponentContext by context {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartMembersFilterInstanceKeeper(items) }
    override val value: Value<List<StartMembersUi>> = keeper.start
    override val filter: MutableValue<List<MembersFilterGroup>> = MutableValue(emptyList())

    init {
        updateFilterGroups()
    }

    override fun goBack() {
        back()
    }

    override fun reset() {

    }

    override fun apply() {

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

    fun updateFilterGroups() {
        val categories = listOf(
            "Distances" to StartMembersUi::distance,
            "Teams" to StartMembersUi::team,
            "City" to StartMembersUi::city,
            "Groups" to StartMembersUi::group
        )

        val updatedFilterGroups = categories.map { (categoryTitle, keyExtractor) ->
            val items = findDuplicates(items, keyExtractor)
                .map { MembersFilterItem.Base(it) }

            MembersFilterGroup(categoryTitle, items)
        }

        filter.value = updatedFilterGroups
    }

    fun <T> findDuplicates(
        items: List<StartMembersUi>,
        keyExtractor: (StartMembersUi) -> T
    ): List<T> {
        return items.groupBy(keyExtractor)
            .filter { it.value.isNotEmpty() }
            .map { it.key }
    }

}