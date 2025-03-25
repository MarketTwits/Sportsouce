package com.markettwits.sportsouce.starts.common.data.mapper

import com.markettwits.sportsouce.starts.cloud.model.NetworkStartRemoteItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem


internal interface StartsCloudToListMapper {
    fun mapSingle(items: List<NetworkStartRemoteItem>): List<StartsListItem>
}