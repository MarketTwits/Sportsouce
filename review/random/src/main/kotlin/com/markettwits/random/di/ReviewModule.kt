package com.markettwits.random.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.random.data.ReviewRepository
import com.markettwits.random.data.ReviewRepositoryBase
import com.markettwits.random.presentation.store.ReviewStoreFactory
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.dsl.module
import com.markettwits.cloud.provider.HttpClientProvider
import org.koin.core.qualifier.named
import ru.alexpanov.core_network.provider.JsonProvider

val reviewModule = module(createdAtStart = true) {
    single<ReviewStoreFactory> {
        ReviewStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get()
        )
    }
    single<ReviewRepository> {
        ReviewRepositoryBase(
            service = get(SportsouceApi::class),
            StartsCloudToUiMapper.Base(
                BaseTimeMapper()
            )
        )
    }
}