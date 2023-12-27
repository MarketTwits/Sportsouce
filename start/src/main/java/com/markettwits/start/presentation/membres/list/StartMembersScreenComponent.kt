package com.markettwits.start.presentation.membres.list

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.operator.map
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import com.markettwits.start.presentation.membres.filter_screen.component.findDuplicates
import kotlinx.serialization.builtins.ListSerializer

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val membersUi: List<StartMembersUi>,
    private val openFilterScreen: (List<MembersFilterGroup>) -> Unit,
    private val onBack: OnClick
) : ComponentContext by componentContext, StartMembersScreen {

    override val members: MutableValue<List<StartMembersUi>> = MutableValue(
        stateKeeper.consume(
            key = "MEMBERS_STATE",
            ListSerializer(StartMembersUi.serializer())
        ) ?: membersUi
    )
    private val filterItems: MutableValue<List<MembersFilterGroup>> = MutableValue(
        stateKeeper.consume(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) ?: emptyList()
    )

    fun updateFilter(filter: List<MembersFilterGroup>) {
        filterItems.value = filter
        //TODO CALL SORT FUN
    }

    init {
        //   applyFilterAndSort()

        stateKeeper.register(
            key = "MEMBERS_STATE",
            ListSerializer(StartMembersUi.serializer())
        ) { members.value }
        stateKeeper.register(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) { filterItems.value }
    }

    override fun filter(value: String) {
        if (value.isEmpty()) {
            members.value = membersUi
        } else {
            val filteredMembers = membersUi.filter {
                it.name.contains(
                    value,
                    ignoreCase = true
                ) || it.surname.contains(value, ignoreCase = true)
            }
            val sortedMembers = filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
            members.value = sortedMembers
        }
    }

    fun updateFilterGroups(): List<MembersFilterGroup> {
        val categories = listOf(
            "Дистанция" to StartMembersUi::distance,
            "Команда" to StartMembersUi::team,
            "Город" to StartMembersUi::city,
            "Группа" to StartMembersUi::group
        )

        val updatedFilterGroups = categories.map { (categoryTitle, keyExtractor) ->
            val items = findDuplicates(membersUi, keyExtractor)
                .map { MembersFilterItem.Base(it) }

            MembersFilterGroup(categoryTitle, items)
        }
        filterItems.value = updatedFilterGroups
        return updatedFilterGroups
    }


    fun <T> findDuplicates(
        items: List<StartMembersUi>,
        keyExtractor: (StartMembersUi) -> T
    ): List<T> {
        return items.groupBy(keyExtractor)
            .filter { it.value.isNotEmpty() }
            .map { it.key }
    }


    override fun openFilter() {
        updateFilterGroups()
        openFilterScreen(filterItems.value)
    }

    override fun back() {
        onBack()
    }


}