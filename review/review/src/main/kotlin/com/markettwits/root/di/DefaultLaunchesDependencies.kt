package com.markettwits.root.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.random.root.di.StartRandomComponentDependencies
import com.markettwits.start_filter.start_filter.di.StartsFilterDependencies
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private class DefaultLaunchesDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartsFilterDependencies

internal val rootModule = module {
    singleOf(::DefaultLaunchesDependencies) bind StartsFilterDependencies::class
    singleOf(::DefaultStartRandomComponentDependencies) bind StartRandomComponentDependencies::class
    single<StartsCloudToUiMapper> { StartsCloudToUiMapper.Base(BaseTimeMapper()) }
}

private class DefaultStartRandomComponentDependencies(
    override val sportsouceApi: SportsouceApi,
    override val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartRandomComponentDependencies