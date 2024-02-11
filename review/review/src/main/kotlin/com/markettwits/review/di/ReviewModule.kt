package com.markettwits.review.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.review.data.ReviewRepository
import com.markettwits.review.data.ReviewRepositoryBase
import com.markettwits.review.presentation.store.ReviewStoreFactory
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.dsl.module

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