package com.markettwits.start_search.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_search.filter.di.StartsFilterDependencies
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

class StartsFilterDependenciesBase(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToListMapper: StartsCloudToListMapper,
) : StartsFilterDependencies