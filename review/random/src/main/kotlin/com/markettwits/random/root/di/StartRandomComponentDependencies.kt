package com.markettwits.random.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.StartsCloudToListMapper

interface StartRandomComponentDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper: StartsCloudToListMapper
}