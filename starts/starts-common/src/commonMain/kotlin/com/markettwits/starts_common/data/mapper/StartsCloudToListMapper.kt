package com.markettwits.starts_common.data.mapper

import com.markettwits.cloud.model.starts.StartRemoteItem
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToListMapper {
    fun mapSingle(items: List<StartRemoteItem>): List<StartsListItem>
}