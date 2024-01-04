package com.markettwits.starts

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.starts.data.StartsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartsInstanceKeeper(
    private val dataSource: StartsDataSource
) : InstanceKeeper.Instance {
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

    fun retry() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            starts.value = StartsUiState.Loading
            dataSource.starts()
        }
    }
}