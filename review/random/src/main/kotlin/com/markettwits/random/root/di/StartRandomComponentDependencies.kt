package com.markettwits.random.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts.data.StartsCloudToUiMapper

interface StartRandomComponentDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper : StartsCloudToUiMapper
}