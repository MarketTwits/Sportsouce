package com.markettwits.start.presentation.membres

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.presentation.start.component.OnClick

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val startId : Int,
    private val service: StartDataSource,
    private val onBack : OnClick
) : ComponentContext by componentContext, StartMembersScreen {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartMembersScreenInstanceKeeper(service, startId) }
    override val members: Value<List<StartMembersUi>> = keeper.start
    override fun back() {
       onBack()
    }


}