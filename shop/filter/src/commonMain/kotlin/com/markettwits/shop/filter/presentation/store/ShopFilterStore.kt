package com.markettwits.shop.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.models.ShopOptionInfo
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Label
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State
import kotlinx.serialization.Serializable

interface ShopFilterStore : Store<Intent, State, Label> {

    @Serializable
    data class State(
        val categories: List<ShopCategoryItem> = emptyList(),
        val options: List<ShopOptionInfo> = emptyList(),
        val selectedPrice: ShopFilterPrice = ShopFilterPrice.EMPTY,
        val selectedOptionUID: List<ShopOptionInfo.Value> = emptyList(),
        val selectedCategoryPath: List<ShopCategoryItem> = emptyList(),
        val isLoading: Boolean = false,
        val isApplied: Boolean = false,
    )

    sealed interface Intent {
        data class OnUpdatePrice(val isMin: Boolean, val value: String) : Intent
        data class OnClickOption(val option: ShopOptionInfo.Value) : Intent
        data object OnClickResetCategory : Intent
        data class OnClickCategory(val category: ShopCategoryItem?) : Intent
        data object OnClickApplyFilter : Intent
        data object OnClickResetFilter : Intent
        data object OnClickGoBack : Intent
    }

    sealed interface Message {
        data class FilterLoaded(
            val categories: List<ShopCategoryItem>,
            val price: ShopFilterPrice,
        ) : Message
        data class UpdateSelectedOption(val selectedOptions: List<ShopOptionInfo.Value>) : Message
        data class UpdateCategories(val categories: List<ShopCategoryItem>) : Message
        data class UpdateCurrentPath(val path: List<ShopCategoryItem>) : Message
        data class UpdateOptionsAndRestrictionPrice(
            val options: List<ShopOptionInfo>,
            val price: ShopFilterPrice,
        ) : Message

        data class UpdatePrice(val price: ShopFilterPrice) : Message
        data object Loading : Message
        data object ResetFilter : Message
        data object ApplyFilter : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class ApplyFilter(val state: State) : Label
    }

}
