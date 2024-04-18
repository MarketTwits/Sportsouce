package com.markettwits.start_filter.start_filter.data.mapper

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.starts_common.domain.StartsListItem

interface StartsCloudToFilterMapper {
    fun map(start: StartsRemote): List<StartsListItem>
}