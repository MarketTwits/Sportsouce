package com.markettwits.review.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.intentActionModule
import com.markettwits.news.di.newsModule
import com.markettwits.review.data.ReviewRepositoryBase
import com.markettwits.review.data.cache.ReviewCache
import com.markettwits.review.domain.ReviewRepository
import com.markettwits.review.presentation.store.ReviewStoreFactory
import com.markettwits.starts_common.di.startsCommonModule
import org.koin.dsl.module

val reviewModule = module {
    includes(intentActionModule, startsCommonModule, newsModule)
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
            newsService = get()
        )
    }
}