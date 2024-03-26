package com.markettwits.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.schedule.schedule.di.StartScheduleComponentDependencies
import com.markettwits.start_filter.start_filter.di.StartsFilterDependencies
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val rootModule = module {
    singleOf(::DefaultStartsFilterDependencies) bind StartsFilterDependencies::class
    singleOf(::DefaultStartRandomComponentDependencies) bind StartRandomComponentDependencies::class
    singleOf(::DefaultStartsScheduleComponentDependencies) bind StartScheduleComponentDependencies::class
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

private class DefaultStartsScheduleComponentDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToListMapper
) : StartScheduleComponentDependencies