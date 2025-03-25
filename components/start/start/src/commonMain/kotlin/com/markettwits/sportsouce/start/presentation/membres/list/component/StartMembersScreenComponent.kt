package com.markettwits.sportsouce.start.presentation.membres.list.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.start.presentation.common.OnClick
import com.markettwits.sportsouce.start.presentation.membres.filter.MembersFilterGroup
import com.markettwits.sportsouce.start.presentation.membres.list.models.MembersFilter
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val membersUi: List<StartMembersUi>,
    private val openFilterScreen: (List<MembersFilterGroup>) -> Unit,
    private val onBack: OnClick,
    private val membersFilter: MembersFilter,
) : ComponentContext by componentContext, StartMembersScreen, BottomBarComponentHandler() {

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }

    override val members: MutableValue<List<StartMembersUi>> = MutableValue(
        stateKeeper.consume(
            key = MEMBERS_STATE_KEY,
            ListSerializer(StartMembersUi.serializer())
        ) ?: membersUi
    )
    private val filterItems: MutableValue<List<MembersFilterGroup>> = MutableValue(
        stateKeeper.consume(
            key = FILTER_STATE_KEY,
            ListSerializer(MembersFilterGroup.serializer())
        ) ?: emptyList()
    )

    override val filterValue: MutableValue<String> = MutableValue(
        stateKeeper.consume(
            key = FILTER_VALUE_STATE_KEY,
            String.serializer()
        ) ?: ""
    )

    fun updateFilter(filter: List<MembersFilterGroup>) {
        filterItems.value = filter
        val sortByKeyWord = membersFilter.filterByKeyWord(filterValue.value, membersUi)
        members.value = membersFilter.filterMembersList(sortByKeyWord, filter)
    }

    init {
        stateKeeper.register(
            key = MEMBERS_STATE_KEY,
            ListSerializer(StartMembersUi.serializer())
        ) { members.value }
        stateKeeper.register(
            key = FILTER_STATE_KEY,
            ListSerializer(MembersFilterGroup.serializer())
        ) { filterItems.value }
        stateKeeper.register(
            key = FILTER_VALUE_STATE_KEY,
            String.serializer()
        ) { filterValue.value }
    }

    override fun handleTextFiled(value: String) {
        filterValue.value = value
        updateFilter(filterItems.value)
    }


    override fun openFilter() {
        if (filterItems.value.isEmpty()) {
            filterItems.value =
                membersFilter.generateFilterGroups(membersUi)
        }
        openFilterScreen(filterItems.value)
    }

    override fun back() {
        onBack()
    }

    private companion object {
        const val MEMBERS_STATE_KEY = "MEMBERS_STATE"
        const val FILTER_STATE_KEY = "FILTER_STATE"
        const val FILTER_VALUE_STATE_KEY = "FILTER_VALUE_STATE"
    }


}