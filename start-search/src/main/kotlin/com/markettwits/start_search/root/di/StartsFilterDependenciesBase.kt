package com.markettwits.start_search.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_filter.start_filter.di.StartsFilterDependencies
import com.markettwits.starts_common.data.StartsCloudToListMapper

class StartsFilterDependenciesBase(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToListMapper: StartsCloudToListMapper,
) : StartsFilterDependencies