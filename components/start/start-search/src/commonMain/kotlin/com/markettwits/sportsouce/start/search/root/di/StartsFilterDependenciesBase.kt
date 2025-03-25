package com.markettwits.sportsouce.start.search.root.di

import com.markettwits.sportsouce.start.search.filter.di.StartsFilterDependencies
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi

class StartsFilterDependenciesBase(
    override val sportsouceApi : SportSauceStartsApi
) : StartsFilterDependencies