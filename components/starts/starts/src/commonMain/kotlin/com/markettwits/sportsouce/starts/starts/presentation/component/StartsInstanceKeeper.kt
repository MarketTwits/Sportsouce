package com.markettwits.sportsouce.starts.starts.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository
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
            dataSource.starts(false, StartsRepository.DEFAULT_STARTS_PAGE)
        }
        dataSource.starts.subscribe {
            starts.value = it
        }
    }

    fun retry(page: Int) {
        scope.launch {
            if (starts.value !is StartsUiState.Success) {
                starts.value = StartsUiState.Loading
            }
            dataSource.starts(true, page)
        }
    }

    fun onPageSelected(page: Int) {
        scope.launch {
            dataSource.starts(false, page)
        }
    }

    fun loadNext(page: Int) {
        scope.launch {
            dataSource.loadNext(page)
        }
    }
}
