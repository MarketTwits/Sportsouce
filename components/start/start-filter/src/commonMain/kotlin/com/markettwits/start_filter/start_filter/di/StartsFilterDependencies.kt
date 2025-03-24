package com.markettwits.start_filter.start_filter.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

interface StartsFilterDependencies {
    val sportsouceApi: SportsouceApi
    val startsCloudToListMapper: StartsCloudToListMapper
}