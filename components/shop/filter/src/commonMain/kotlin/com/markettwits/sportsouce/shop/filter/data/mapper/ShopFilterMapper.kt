package com.markettwits.sportsouce.shop.filter.data.mapper

import com.markettwits.sportsouce.shop.cloud.model.categories.ChildrenItem
import com.markettwits.sportsouce.shop.cloud.model.common.OptionInfo
import com.markettwits.sportsouce.shop.cloud.model.renderFilter.RenderFilterRemote
import com.markettwits.sportsouce.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.sportsouce.shop.filter.domain.models.ShopOptionInfo

interface ShopFilterMapper {

    fun mapRenderFilter(renderFilter: RenderFilterRemote): Pair<List<ShopOptionInfo>, ShopFilterPrice>

    fun mapCategories(categories: List<ChildrenItem>): List<ShopCategoryItem>

    fun mapOptions(options: List<OptionInfo>): List<ShopOptionInfo>


}