package com.markettwits.starts.starts.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.starts.starts.domain.StartsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartsInstanceKeeper(
    private val dataSource: StartsRepository,
) : InstanceKeeper.Instance {
    val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        scope.launch {
            dataSource.starts(false)
        }
        dataSource.starts.subscribe {
            starts.value = it
        }
    }

    fun retry() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            if (starts.value is StartsUiState.Success) {
                dataSource.starts(true)
            } else {
                starts.value = StartsUiState.Loading
                dataSource.starts(true)
            }
        }
    }
}