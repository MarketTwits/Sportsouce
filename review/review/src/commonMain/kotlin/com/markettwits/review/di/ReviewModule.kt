package com.markettwits.review.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.intentActionModule
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.review.data.ReviewRepositoryBase
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.review.data.mapper.ReviewMapperBase
import com.markettwits.review.domain.ReviewRepository
import com.markettwits.review.presentation.store.ReviewStoreFactory
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.time.ExtendedTimeMapper
import org.koin.dsl.module

val reviewModule = module(createdAtStart = true) {
    includes(intentActionModule)
    single<ReviewStoreFactory> {
        ReviewStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get(),
            intentAction = get()
        )
    }
    single<ReviewRepository> {
        ReviewRepositoryBase(
            service = get(SportsouceApi::class),
            cache = ReviewCache(),
            executor = ExecuteWithCacheBase(),
            reviewMapper = ReviewMapperBase(
                startsMapper = StartsCloudToListMapperBase(
                   ExtendedTimeMapper()
                ),
                newsMapper = NewsRemoteToDomainMapper.Base(
                    ExtendedTimeMapper()
                )
            ),
        )
    }
}