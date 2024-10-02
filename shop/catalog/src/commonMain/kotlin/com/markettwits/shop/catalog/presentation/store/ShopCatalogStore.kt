package com.markettwits.shop.catalog.presentation.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.catalog.domain.models.ShopCatalogOptions
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import kotlinx.coroutines.flow.Flow

interface ShopCatalogStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        val shopItems: Flow<PagingData<ShopItem>>,
        val options: ShopCatalogOptions,
        val categories: List<ShopCategoryItem> = emptyList(),
        val filterState: ShopFilterStore.State? = null,
    )

    sealed interface Intent {
        data class OnClickItem(val id: String) : Intent
        data class OnClickCategory(val categoryId: Int) : Intent
        data object OnClickFilter : Intent
        data class ApplyFilter(val state: ShopFilterStore.State) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val items: Flow<PagingData<ShopItem>>) : Message
        data class CategoriesLoaded(val items: List<ShopCategoryItem>) : Message
        data class UpdateCategories(val id: Int) : Message
        data class Failed(val message: String) : Message
        data class FilterApplied(val filterState: ShopFilterStore.State) : Message
    }

    sealed interface Label {
        data class OnClickItem(val id: String) : Label
        data object GoBack : Label
        data class GoFilter(val filterState: ShopFilterStore.State?) : Label
    }

}
