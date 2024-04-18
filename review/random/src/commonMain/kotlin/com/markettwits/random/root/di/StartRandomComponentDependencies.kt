package com.markettwits.random.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

interface StartRandomComponentDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper: StartsCloudToListMapper
}