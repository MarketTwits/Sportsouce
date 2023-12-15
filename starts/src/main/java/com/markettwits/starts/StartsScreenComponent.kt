package com.markettwits.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.starts.data.StartsDataSource

class StartsScreenComponent(
    val componentContext: ComponentContext,
    val dataSource: StartsDataSource,
    val toDetail: (Int) -> Unit
) : ComponentContext by componentContext, StartsScreen {

    private val someLogic =
        instanceKeeper.getOrCreateSimple { StartsInstanceKeeper(dataSource) }

    override fun onItemClick(startId: Int) {
        toDetail(startId)
    }
    override val starts: Value<StartsUiState> = someLogic.starts

}
