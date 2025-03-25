package com.markettwits.sportsouce.shop.filter.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.categories.ChildrenItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem

interface ShopProductsCategoriesMapper {

    fun map(categories: List<ChildrenItem>): List<ShopCategoryItem>

}