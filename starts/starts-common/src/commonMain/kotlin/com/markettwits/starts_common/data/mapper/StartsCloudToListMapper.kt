package com.markettwits.starts_common.data.mapper

import com.markettwits.cloud.model.NetworkStartRemoteItem
import com.markettwits.starts_common.domain.StartsListItem


internal interface StartsCloudToListMapper {
    fun mapSingle(items: List<NetworkStartRemoteItem>): List<StartsListItem>
}