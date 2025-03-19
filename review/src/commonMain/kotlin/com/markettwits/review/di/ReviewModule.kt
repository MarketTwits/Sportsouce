package com.markettwits.review.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.intentActionModule
import com.markettwits.news.cloud.sportSauceNewsNetworkModule
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.review.data.ReviewRepositoryBase
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.review.data.mapper.ReviewMapperBase
import com.markettwits.review.domain.ReviewRepository
import com.markettwits.review.presentation.store.ReviewStoreFactory
import com.markettwits.starts_common.di.startsCommonModule
import com.markettwits.time.BaseTimeMapper
import org.koin.dsl.module

val reviewModule = module {
    includes(intentActionModule, startsCommonModule, sportSauceNewsNetworkModule)
    single<ReviewStoreFactory> {
        ReviewStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get(),
            intentAction = get()
        )
    }
    single<ReviewRepository> {
        ReviewRepositoryBase(
            startsService = get(),
            cache = ReviewCache(),
            executor = ExecuteWithCacheBase(),
            reviewMapper = ReviewMapperBase(
                newsMapper = NewsRemoteToDomainMapper.Base(
                    BaseTimeMapper()
                )
            ),
            newsService = get()
        )
    }
}