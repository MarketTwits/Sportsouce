package com.markettwits.shop.filter.domain.mapper

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.shop.filter.domain.models.ShopCategoryItem

class ShopProductsCategoriesMapperBase : ShopProductsCategoriesMapper {

    override fun map(categories: List<ChildrenItem>): List<ShopCategoryItem> =
        categories.map { convertChildrenItemToCategoryItem(it) }

    private fun convertChildrenItemToCategoryItem(childrenItem: ChildrenItem): ShopCategoryItem {
        return ShopCategoryItem(
            children = childrenItem.children.map { convertChildrenItemToCategoryItem(it) },
            description = childrenItem.description?: "",
            id = childrenItem.id,
            level = childrenItem.level ?: 1,
            parentId = childrenItem.parent_id,
            slug = childrenItem.slug ?: "",
            title = childrenItem.title
        )
    }
}