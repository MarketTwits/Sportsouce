package com.markettwits.starts_common.data

import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToListMapper {
    fun mapSingle(items: List<Row>): List<StartsListItem>
}