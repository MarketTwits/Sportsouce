package com.markettwits.start

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.cloud.model.start.StartData
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.start.data.StartDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartScreenComponent(
    componentContext: ComponentContext,
    private val startId: Int,
    private val service: StartDataSource
) :
    ComponentContext by componentContext, StartScreen {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartScreenInstanceKeeper(service, startId) }

    override val start: Value<StartRemote> = keeper.start

}