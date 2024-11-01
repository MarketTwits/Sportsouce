package com.markettwits.shop.filter.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.models.ShopFilterResult
import com.markettwits.shop.filter.domain.models.ShopOptionInfo
import com.markettwits.shop.filter.domain.models.mapToStringOptions
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Label
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Message
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State
import kotlinx.coroutines.launch

abstract class ShopFilterExecutorHandler(private val repository: ShopFilterRepository) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    fun onClickUpdateOption(
        option: ShopOptionInfo.Value,
        options: List<ShopOptionInfo.Value>,
    ) {
        val newOptions = options.let {
            val valueExists = it.any { value -> option.uuid == value.uuid }
            if (valueExists) {
                it.filterNot { value -> option.uuid == value.uuid }
            } else {
                it + option
            }
        }
        dispatch(Message.UpdateSelectedOption(newOptions))
    }


    fun onClickUpdatePrice(
        isMin: Boolean,
        value: String,
        previousPrice: ShopFilterPrice,
    ) {
        val price =
            if (value.isEmpty())
                ShopFilterPrice.Value.Empty
            else
                ShopFilterPrice.Value.Price(value.toInt())
        val newPrice = if (isMin)
            previousPrice.copy(minPrice = price)
        else
            previousPrice.copy(maxPrice = price)
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
                    publish(Label.GoBack)
                })
        }
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
        publish(Label.ApplyFilter(currentState.mapToFilterResult()))
    }

    fun onClickGoBack() = publish(Label.GoBack)

    fun onClickResetFilter() = dispatch(Message.ResetFilter)

    fun onClickResetCategory() {
        dispatch(Message.UpdateCurrentPath(emptyList()))
    }

    fun firstLaunch() {
        scope.launch {
            dispatch(Message.Loading)
            repository.filter().onSuccess {
                dispatch(Message.FilterLoaded(it, ShopFilterPrice.EMPTY))
            }.onFailure {
                publish(Label.GoBack)
            }
        }
    }
}

internal fun State.mapToFilterResult(): ShopFilterResult =
    ShopFilterResult(
        categoryId = if (selectedCategoryPath.isNotEmpty())
            selectedCategoryPath.last().id
        else null,
        options = selectedOptionUID.mapToStringOptions(),
        maxPrice = selectedPrice.maxPrice.apply(),
        minPrice = selectedPrice.minPrice.apply()
    )
