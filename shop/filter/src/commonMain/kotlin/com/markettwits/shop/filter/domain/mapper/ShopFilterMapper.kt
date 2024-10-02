package com.markettwits.shop.filter.domain.mapper

import com.markettwits.cloud_shop.model.categories.ChildrenItem
import com.markettwits.cloud_shop.model.common.OptionInfo
import com.markettwits.cloud_shop.model.renderFilter.RenderFilterRemote
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.domain.models.ShopOptionInfo

interface ShopFilterMapper {

    fun mapRenderFilter(renderFilter: RenderFilterRemote): Pair<List<ShopOptionInfo>, ShopFilterPrice>

    fun mapCategories(categories: List<ChildrenItem>): List<ShopCategoryItem>

    fun mapOptions(options: List<OptionInfo>): List<ShopOptionInfo>


}