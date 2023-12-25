package com.markettwits.start.presentation.membres.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.presentation.common.OnClick

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val startId : Int,
    private val membersUi: List<StartMembersUi>,
    private val service: StartDataSource,
    private val openFilterScreen : OnClick,
    private val onBack : OnClick
) : ComponentContext by componentContext, StartMembersScreen {
    private val keeper =
        instanceKeeper.getOrCreateSimple { StartMembersScreenInstanceKeeper(service, startId, membersUi) }
    override val members: Value<List<StartMembersUi>> = keeper.start
    override fun filter(value: String) {
        keeper.filter(value)
    }

    override fun openFilter() {
        openFilterScreen()
    }

    override fun back() {
       onBack()
    }


}