package com.markettwits.schedule.schedule.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

interface StartScheduleComponentDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper: StartsCloudToListMapper
}