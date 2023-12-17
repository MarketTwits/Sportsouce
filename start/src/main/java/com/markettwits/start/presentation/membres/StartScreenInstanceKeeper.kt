package com.markettwits.start.presentation.membres

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartMembersScreenInstanceKeeper(
    private val service: StartDataSource,
    private val startId: Int
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<List<StartMembersUi>> = MutableValue(emptyList())

    init {
        scope.launch {
            start.value = service.startMember(startId)
        }
    }
}