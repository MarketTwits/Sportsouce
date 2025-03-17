package com.markettwits.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.start_search.filter.di.StartsFilterDependencies
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.time.BaseTimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val reviewRootModule = module {
    singleOf(::DefaultStartsFilterDependencies) bind StartsFilterDependencies::class
    singleOf(::DefaultStartRandomComponentDependencies) bind StartRandomComponentDependencies::class
    single<StartsCloudToListMapper> { StartsCloudToListMapperBase(BaseTimeMapper()) }
}

private class DefaultStartsFilterDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToListMapper: StartsCloudToListMapper,
) : StartsFilterDependencies

private class DefaultStartRandomComponentDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToListMapper
) : StartRandomComponentDependencies
