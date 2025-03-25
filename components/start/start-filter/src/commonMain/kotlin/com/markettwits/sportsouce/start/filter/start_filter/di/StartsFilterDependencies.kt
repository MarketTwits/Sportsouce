package com.markettwits.sportsouce.start.filter.start_filter.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapper

interface StartsFilterDependencies {
    val sportsouceApi: SportsouceApi
    val startsCloudToListMapper: StartsCloudToListMapper
}