package com.markettwits.start.presentation.start

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.data.StartDataSource

class StartScreenComponent(
    componentContext: ComponentContext,
    private val startId: Int,
    private val service: StartDataSource,
    private val back : () -> Unit,
    private val members : (Int) -> Unit
) :
    ComponentContext by componentContext, StartScreen {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartScreenInstanceKeeper(service, startId) }

    override val start: Value<StartItemUi> = keeper.start
    override fun goBack() {
        back()
    }

    override fun goMembers() {
        members(startId)
    }

}