package com.markettwits.starts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.markettwits.start.StartScreenComponent
import com.markettwits.starts.data.BaseTimeMapper
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.data.StartsDataSource
import kotlinx.serialization.Serializable
import ru.alexpanov.core_network.api.SportSouceReposiotryImpl
import ru.alexpanov.core_network.api.SportsouceApi
import ru.alexpanov.core_network.model.all.StartsCloud
import ru.alexpanov.core_network.provider.HttpClientProvider2
import ru.alexpanov.core_network.provider.JsonProvider

class StartsScreenComponent(
    val componentContext: ComponentContext,
    val repository: SportsouceApi,
    val dataSource: StartsDataSource,
    val toDetail: (Int) -> Unit
) : ComponentContext by componentContext, StartsScreen {

    private val someLogic =
        instanceKeeper.getOrCreateSimple { StartHandler(repository, dataSource) }

    override fun onItemClick(startdId: Int) {
        toDetail(startdId)
    }

    override val data: MutableValue<StartsCloud> = someLogic.state
    override val starts: Value<StartsUiState> = someLogic.starts

}
