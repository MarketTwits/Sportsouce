package com.markettwits.sportsouce.review.review.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.starts.albums.di.startsAlbumsDataModule
import com.markettwits.sportsouce.review.review.data.ReviewPhotoAlbumsRepositoryBase
import com.markettwits.sportsouce.review.review.data.cache.ReviewPhotoAlbumsCache
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbumsRepository
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.news.di.newsModule
import com.markettwits.sportsouce.review.review.data.ReviewRepositoryBase
import com.markettwits.sportsouce.review.review.data.cache.ReviewCache
import com.markettwits.sportsouce.review.review.domain.ReviewRepository
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStoreFactory
import com.markettwits.sportsouce.shop.catalog.di.shopCatalogModule
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import org.koin.dsl.module

val reviewModule = module {
    includes(
        intentActionModule,
        startsCommonModule,
        shopCatalogModule,
        newsModule,
        startsAlbumsDataModule
    )
    single<ReviewStoreFactory> {
        ReviewStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get(),
            intentAction = get(),
            photoAlbumsRepository = get()
        )
    }
    single<ReviewPhotoAlbumsRepository> { ReviewPhotoAlbumsRepositoryBase(get(), ReviewPhotoAlbumsCache()) }
    single<ReviewRepository> {
        ReviewRepositoryBase(
            startsService = get(),
            cache = ReviewCache(),
            productsService = get(),
            newsService = get()
        )
    }
}
