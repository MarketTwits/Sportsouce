package com.markettwits.start_search.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_filter.start_filter.di.StartsFilterDependencies

class StartsFilterDependenciesBase(
    override val sportsouceApi: SportsouceApi,
) : StartsFilterDependencies