package com.markettwits.schedule.schedule.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.schedule.schedule.data.ScheduleRepositoryBase
import com.markettwits.schedule.schedule.data.cache.StartsScheduleCache
import com.markettwits.schedule.schedule.domain.ScheduleRepository
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStoreFactory
import com.markettwits.starts_common.domain.StartsListItem
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startScheduleModule = module {
    factoryOf(::ScheduleRepositoryBase) bind ScheduleRepository::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartDetailScheduleStoreFactory)
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    single<ObservableCache<List<StartsListItem>>> {
        StartsScheduleCache()
    }


}

internal fun createStartRandomModule(dependencies: StartScheduleComponentDependencies): List<Module> {
    return listOf(
        startScheduleModule,
        module {
            factory { dependencies.startsCloudToUiMapper }
            factory { dependencies.sportsouceApi }
        }
    )
}