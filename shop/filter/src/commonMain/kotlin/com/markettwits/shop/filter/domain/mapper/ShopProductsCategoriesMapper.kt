package com.markettwits.shop.filter.domain.mapper

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.shop.filter.domain.models.ShopCategoryItem

interface ShopProductsCategoriesMapper {

    fun map(categories: List<ChildrenItem>): List<ShopCategoryItem>

}