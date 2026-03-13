package com.markettwits.sportsouce.starts.starts.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository

class StartsScreenComponent(
    componentContext: ComponentContext,
    private val dataSource: StartsRepository,
    private val toDetail: (StartsListItem) -> Unit,
    private val toSearch: () -> Unit,
    private val toSettings: () -> Unit,
) : ComponentContext by componentContext, StartsScreen {

    private val someLogic =
        instanceKeeper.getOrCreateSimple { StartsInstanceKeeper(dataSource) }

    override fun onItemClick(startItem: StartsListItem) {
        toDetail(startItem)
    }

    override fun onSearchClick() {
        toSearch()
    }

    override fun onSettingsClick() {
        toSettings()
    }

    override fun retry(page: Int) {
        someLogic.retry(page)
    }

    override fun onPageSelected(page: Int) {
        someLogic.onPageSelected(page)
    }

    override fun onLoadNext(page: Int) {
        someLogic.loadNext(page)
    }

    override val starts: Value<StartsUiState> = someLogic.starts

}
