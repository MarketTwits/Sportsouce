package com.markettwits.schedule.schedule.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts.data.StartsCloudToUiMapper

interface StartScheduleComponentDependencies {
    val sportsouceApi : SportsouceApi
    val startsCloudToUiMapper : StartsCloudToUiMapper
}