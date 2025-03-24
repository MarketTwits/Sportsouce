package com.markettwits.root.di

import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.start_search.filter.di.StartsFilterDependencies
import com.markettwits.starts_common.di.startsCommonModule
import com.markettwits.starts_common.domain.SportSauceStartsApi
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
