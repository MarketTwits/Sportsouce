package com.markettwits.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.schedule.schedule.di.StartScheduleComponentDependencies
import com.markettwits.start_filter.start_filter.di.StartsFilterDependencies
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val rootModule = module {
    singleOf(::DefaultLaunchesDependencies) bind StartsFilterDependencies::class
    singleOf(::DefaultStartRandomComponentDependencies) bind StartRandomComponentDependencies::class
    singleOf(::DefaultStartsScheduleComponentDependencies) bind StartScheduleComponentDependencies::class
    single<StartsCloudToUiMapper> { StartsCloudToUiMapper.Base(BaseTimeMapper()) }
}

private class DefaultLaunchesDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartsFilterDependencies

private class DefaultStartRandomComponentDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartRandomComponentDependencies

private class DefaultStartsScheduleComponentDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartScheduleComponentDependencies