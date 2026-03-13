package com.markettwits.sportsouce.shop.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.shop.filter.domain.ShopFilterRepository
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore.*

class ShopFilterStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopFilterRepository,
) {
    fun create(initialCategoryId: Int? = null): ShopFilterStore = ShopFilterStoreImpl(initialCategoryId)

    private inner class ShopFilterStoreImpl(initialCategoryId: Int?) :
        ShopFilterStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopFilterStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopFilterExecutor(repository, initialCategoryId) },
            reducer = ShopFilterReducer
        )

    internal companion object {
        fun findCategoryPath(categories: List<ShopCategoryItem>, targetId: Int): List<ShopCategoryItem> {
            categories.forEach { category ->
                if (category.id == targetId) {
                    return listOf(category)
                }
                val childPath = findCategoryPath(category.children, targetId)
                if (childPath.isNotEmpty()) {
                    return listOf(category) + childPath
                }
            }
            return emptyList()
        }
    }
}
