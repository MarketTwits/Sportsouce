package com.markettwits.shop.catalog.domain.mapper

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem

interface ShopProductsCategoriesMapper {

    fun map(categories: List<ChildrenItem>): List<ShopCategoryItem>

}