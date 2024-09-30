package com.markettwits.shop.filter.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Label
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Message
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State
import kotlinx.coroutines.launch

abstract class ShopFilterExecutorHandler(private val repository: ShopFilterRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    fun onClickUpdateOption(
        uuid: String,
        options: Set<String>,
    ) {
        val newOptions = options.let {
            if (it.contains(uuid)) {
                it - uuid
            } else {
                it + uuid
            }
        }
        dispatch(Message.UpdateSelectedOption(newOptions))
    }


    fun onClickUpdatePrice(
        isMin: Boolean,
        value: Int,
        previousPrice: ShopFilterPrice,
    ) {
        val newPrice = if (isMin)
            previousPrice.copy(minPrice = ShopFilterPrice.Value.Price(value))
        else
            previousPrice.copy(maxPrice = ShopFilterPrice.Value.Price(value))
        dispatch(Message.UpdatePrice(newPrice))
    }

    private fun updateOptionsByCategory(
        categoryId: Int,
    ) {
        scope.launch {
            dispatch(Message.Loading)
            repository.options(categoryId).fold(
                onSuccess = {
                    dispatch(Message.UpdateOptionsAndRestrictionPrice(it.first, it.second))
                }, onFailure = {
                    println("FAILURE")
                    println(it.message.toString())
                })
        }
    }

    fun setCategories(categories: List<ShopCategoryItem>) {
        dispatch(Message.UpdateCategories(categories))
    }

    fun onClickCategory(currentPath: List<ShopCategoryItem>, category: ShopCategoryItem?) {
        if (category == null) {
            dispatch(Message.UpdateCurrentPath(emptyList()))
        } else {
            val index = currentPath.indexOf(category)
            if (index != -1) {
                dispatch(Message.UpdateCurrentPath(currentPath.take(index + 1)))
            } else {
                dispatch(Message.UpdateCurrentPath(currentPath + category))
            }
            updateOptionsByCategory(category.id)
        }
    }

    fun onClickApplyFilter(currentState: State) {
        publish(Label.ApplyFilter(currentState))
    }

    fun onClickGoBack() = publish(Label.GoBack)

    fun onClickResetFilter() {
        dispatch(Message.ResetFilter)
    }


    fun firstLaunch() {
        scope.launch {
            dispatch(Message.Loading)
            repository.filter().onSuccess {
                dispatch(Message.FilterLoaded(it, ShopFilterPrice.EMPTY))
            }
        }
    }
}