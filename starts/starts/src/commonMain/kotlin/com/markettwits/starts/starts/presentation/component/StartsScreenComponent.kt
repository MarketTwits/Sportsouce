package com.markettwits.starts.starts.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.starts.starts.domain.StartsRepository

class StartsScreenComponent(
    componentContext: ComponentContext,
    private val dataSource: StartsRepository,
    private val toDetail: (Int) -> Unit,
    private val toSearch: () -> Unit,
) : ComponentContext by componentContext, StartsScreen {

    private val someLogic =
        instanceKeeper.getOrCreateSimple { StartsInstanceKeeper(dataSource) }

    override fun onItemClick(startId: Int) {
        toDetail(startId)
    }

    override fun onSearchClick() {
        toSearch()
    }

    override fun retry() {
        someLogic.retry()
    }

    override val starts: Value<StartsUiState> = someLogic.starts

}
