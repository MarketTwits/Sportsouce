package com.markettwits.starts.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.root.api.RootStartsComponent
import com.markettwits.starts.data.StartsRepository

class StartsScreenComponent(
    componentContext: ComponentContext,
    private val dataSource: StartsRepository,
    private val launchPolicy: RootStartsComponent.LaunchPolicy,
    private val toDetail: (Int) -> Unit
) : ComponentContext by componentContext, StartsScreen {

    private val someLogic =
        instanceKeeper.getOrCreateSimple { StartsInstanceKeeper(dataSource, launchPolicy) }

    override fun onItemClick(startId: Int) {
        toDetail(startId)
    }

    override fun retry() {
        someLogic.retry()
    }

    override val starts: Value<StartsUiState> = someLogic.starts

}
