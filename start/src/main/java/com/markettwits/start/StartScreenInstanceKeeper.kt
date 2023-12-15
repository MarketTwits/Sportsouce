package com.markettwits.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.cloud.model.start.StartData
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.start.data.StartDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexpanov.core_network.model.all.StartsRemote

class StartScreenInstanceKeeper(
    private val service: StartDataSource,
    private val startId: Int
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<StartRemote> = MutableValue(StartRemote(null))

    init {
        scope.launch {
            start.value = service.start(startId)
        }
    }
}