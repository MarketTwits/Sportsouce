package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.data.StartDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartScreenInstanceKeeper(
    private val service: StartDataSource,
    private val startId: Int
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<StartItemUi> = MutableValue(StartItemUi.Loading)

    init {
        scope.launch {
            start.value = service.start(startId)
        }
    }
}