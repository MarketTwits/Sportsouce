package com.markettwits.start_filter.start_filter.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts.data.StartsCloudToUiMapper

interface StartsFilterDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper : StartsCloudToUiMapper
}