package com.markettwits.starts_common.data.mapper

import com.markettwits.cloud.model.starts.Row
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToListMapper {
    fun mapSingle(items: List<Row>): List<StartsListItem>
}