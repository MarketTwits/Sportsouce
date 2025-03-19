package com.markettwits.start_search.root.di

import com.markettwits.start_search.filter.di.StartsFilterDependencies
import com.markettwits.starts_common.domain.SportSauceStartsApi

class StartsFilterDependenciesBase(
    override val sportsouceApi : SportSauceStartsApi
) : StartsFilterDependencies