package com.markettwits.sportsouce.start.presentation.membres.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.start.presentation.common.OnClick
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterGroup
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.membres.store.StartMembersStoreFactory
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterItem
import com.markettwits.sportsouce.start.presentation.membres.store.StartMembersStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val startId: Int,
    private val initialMembersUi: List<StartMembersUi>,
    private val onBack: OnClick,
    private val storeFactory: StoreFactory,
    private val repository: StartRepository,
) : ComponentContext by componentContext, StartMembersScreen, BottomBarComponentHandler() {


    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        StartMembersStoreFactory(
            storeFactory = storeFactory,
            repository = repository,
            startId = startId
        ).create()
    }

    @ExperimentalCoroutinesApi
    override val state: StateFlow<StartMembersStore.State> = store.stateFlow

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }

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

    private val _showFilterDialog = MutableValue(false)
    val showFilterDialog: Value<Boolean> = _showFilterDialog

    fun updateFilter(filter: List<MembersFilterGroup>) {
        filterItems.value = filter
        store.accept(StartMembersStore.Intent.ApplyFilters(filter))
    }

    init {
        stateKeeper.register(
            key = FILTER_STATE_KEY,
            ListSerializer(MembersFilterGroup.serializer())
        ) { filterItems.value }
        stateKeeper.register(
            key = FILTER_VALUE_STATE_KEY,
            String.serializer()
        ) { filterValue.value }

        // collect labels and state
        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is StartMembersStore.Label.Back -> onBack()
                }
            }
        }
        scope.launch {
            store.stateFlow.collect { st ->
                if (filterItems.value.isEmpty() && st.filtersUi.isNotEmpty()) {
                    filterItems.value = st.filtersUi
                }
                if (filterValue.value != st.queryState) {
                    filterValue.value = st.queryState
                }
            }
        }
        // trigger initial loading
        store.accept(StartMembersStore.Intent.Init)
    }

    override fun handleTextFiled(value: String) {
        filterValue.value = value
        store.accept(StartMembersStore.Intent.QueryChanged(value))
    }


    override fun openFilter() {
        _showFilterDialog.value = true
    }

    fun closeFilter() {
        _showFilterDialog.value = false
    }

    fun toggleFilter(groupTitle: String, itemTitle: String) {
        val currentFilters = filterItems.value.toMutableList()
        val groupIndex = currentFilters.indexOfFirst { it.title == groupTitle }
        if (groupIndex != -1) {
            val group = currentFilters[groupIndex]
            val newItems = group.items.map { item ->
                if (item.title == itemTitle) {
                    when (item) {
                        is MembersFilterItem.Base ->
                            MembersFilterItem.Selected(item.title)

                        is MembersFilterItem.Selected ->
                            MembersFilterItem.Base(item.title)
                    }
                } else item
            }
            currentFilters[groupIndex] = group.copy(items = newItems)
            updateFilter(currentFilters)
        }
    }

    override fun removeSelectedFilter(title: String) {
        store.accept(StartMembersStore.Intent.RemoveSelectedFilter(title))
    }

    override fun back() {
        onBack()
    }

    private companion object {
        const val FILTER_STATE_KEY = "FILTER_STATE"
        const val FILTER_VALUE_STATE_KEY = "FILTER_VALUE_STATE"
    }


}