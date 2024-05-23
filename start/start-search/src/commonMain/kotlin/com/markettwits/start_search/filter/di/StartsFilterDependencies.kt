package com.markettwits.start_search.filter.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

interface StartsFilterDependencies {
    val sportsouceApi: SportsouceApi
    val startsCloudToListMapper: StartsCloudToListMapper
}