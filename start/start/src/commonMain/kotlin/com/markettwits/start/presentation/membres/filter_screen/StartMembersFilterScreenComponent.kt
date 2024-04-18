package com.markettwits.start.presentation.membres.filter_screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.serialization.builtins.ListSerializer

class StartMembersFilterScreenComponent(
    private val context: ComponentContext,
    private val back: () -> Unit,
    private val items: List<MembersFilterGroup>,
    private val apply: (List<MembersFilterGroup>) -> Unit,
    private val handleMembersFilter: HandleMembersFilter
) : StartMembersFilterScreen, ComponentContext by context {

    override val filter: MutableValue<List<MembersFilterGroup>> = MutableValue(
        stateKeeper.consume(
            key = FILTER_STATE_KEY,
            ListSerializer(MembersFilterGroup.serializer())
        ) ?: items
    )

    init {
        stateKeeper.register(
            key = FILTER_STATE_KEY,
            ListSerializer(MembersFilterGroup.serializer())
        ) { filter.value }
    }

    override fun goBack() {
        back()
    }

    override fun reset() {
        val baseFilters = filter.value.map { handleMembersFilter.convertToBaseFilter(it) }
        apply(baseFilters)
    }

    override fun apply() {
        apply(filter.value)
    }

    override fun toggleFilterItemState(categoryIndex: Int, itemIndex: Int) {
        filter.value = handleMembersFilter.changeState(filter.value, categoryIndex, itemIndex)
    }

    private companion object {
        const val FILTER_STATE_KEY = "FILTER_STATE"
    }

}