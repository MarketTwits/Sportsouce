package com.markettwits.shop.filter.domain.mapper

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.cloud_shop.model.renderFilter.PriceDiapason
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote
import com.markettwits.shop.catalog.domain.models.ShopCategoryItem
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice
import com.markettwits.shop.catalog.domain.models.ShopOptionInfo

class ShopFilterMapperBase : ShopFilterMapper {

    override fun mapRenderFilter(renderFilter: RenderFilterRemote): Pair<List<ShopOptionInfo>, ShopFilterPrice> {
        val price = mapPriceDiapason(renderFilter.priceDiapason)
        val options = mapOptions(renderFilter.filters.rows)
        return Pair(options, price)
    }


    override fun mapCategories(categories: List<ChildrenItem>): List<ShopCategoryItem> =
        categories.map { convertChildrenItemToCategoryItem(it) }

    override fun mapOptions(options: List<OptionInfo>): List<ShopOptionInfo> = options.map {
        ShopOptionInfo(
            createdAt = it.createdAt,
            inFilter = it.in_filter,
            name = it.name,
            updatedAt = it.updatedAt,
            uuid = it.uuid,
            values = it.values?.map { value ->
                ShopOptionInfo.Value(
                    createdAt = value.createdAt,
                    name = value.name,
                    productOptionUuid = value.product_option_uuid,
                    updatedAt = it.updatedAt,
                    uuid = value.uuid
                )
            } ?: emptyList()
        )
    }

    private fun convertChildrenItemToCategoryItem(childrenItem: ChildrenItem): ShopCategoryItem {
        return ShopCategoryItem(
            children = childrenItem.children.map { convertChildrenItemToCategoryItem(it) },
            description = childrenItem.description,
            id = childrenItem.id,
            level = childrenItem.level,
            parentId = childrenItem.parent_id,
            slug = childrenItem.slug,
            title = childrenItem.title
        )
    }

    private fun mapPriceDiapason(priceDiapason: PriceDiapason): ShopFilterPrice {
        val max = priceDiapason.max
        val maxValue = if (max != null) ShopFilterPrice.Value.Price(
            mapIntToRubblesWithoutPennies(max)
        ) else ShopFilterPrice.Value.Empty

        val min = priceDiapason.min
        val minvalue = if (min != null) ShopFilterPrice.Value.Price(
            mapIntToRubblesWithoutPennies(min)
        ) else ShopFilterPrice.Value.Empty

        return ShopFilterPrice(minvalue, maxValue)

    }

    private fun mapIntToRubblesWithoutPennies(value: Int): Int = value / 100
}