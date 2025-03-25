package com.markettwits.sportsouce.review.root.di

import com.markettwits.sportsouce.start.search.filter.di.StartsFilterDependencies
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.random.root.di.StartRandomComponentDependencies
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val reviewRootModule = module {
    includes(startsCommonModule)
    singleOf(::DefaultStartsFilterDependencies) bind StartsFilterDependencies::class
    singleOf(::DefaultStartRandomComponentDependencies) bind StartRandomComponentDependencies::class
}

private class DefaultStartsFilterDependencies(
    override val sportsouceApi : SportSauceStartsApi
) : StartsFilterDependencies

private class DefaultStartRandomComponentDependencies(
    override val sportsouceApi : SportSauceStartsApi
) : StartRandomComponentDependencies
