package com.markettwits.starts

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.starts.data.StartsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexpanov.core_network.api.SportsouceApi
import ru.alexpanov.core_network.model.all.StartsCloud

class StartHandler(
    private val repository: SportsouceApi,
    private val dataSource: StartsDataSource
) : InstanceKeeper.Instance {
    val state: MutableValue<StartsCloud> = MutableValue(StartsCloud(0, emptyList()))
    val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    private val scope = CoroutineScope(Dispatchers.Main)
    init {
        scope.launch {
            dataSource.starts()
        }
        dataSource.starts.subscribe {
            starts.value = it
        }
    }

    private fun fetch() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val starts = repository.fetchStarts()
            state.value = starts
        }
    }
    private fun loadStarts(){
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {

        }
//        dataSource.starts()
    }
}