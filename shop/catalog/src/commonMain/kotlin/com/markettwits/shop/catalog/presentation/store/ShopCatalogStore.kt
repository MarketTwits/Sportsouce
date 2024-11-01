package com.markettwits.shop.catalog.presentation.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterResult
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import kotlinx.coroutines.flow.Flow

interface ShopCatalogStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        val shopItems: Flow<PagingData<ShopItem>>,
        val queryState : String = "",
    )

    sealed interface Intent {
        data object OnClickGoBack : Intent
        data object OnClickSearch : Intent
        data class OnClickItem(val item: ShopItem) : Intent
        data object OnClickFilter : Intent
        data class ApplyFilter(val state: ShopFilterResult) : Intent
        data class ApplyQuery(val query : String) : Intent

    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val items: Flow<PagingData<ShopItem>>) : Message
        data class UpdateCategories(val id: Int) : Message
        data class Failed(val message: String) : Message
        data class QueryApplied(val query: String) : Message
    }

    sealed interface Label {
        data class OnClickItem(val item: ShopItem) : Label
        data object GoBack : Label
        data class GoSearch(val query: String) : Label
        data object GoFilter : Label
    }

}
