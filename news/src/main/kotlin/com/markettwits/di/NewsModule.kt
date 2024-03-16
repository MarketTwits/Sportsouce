package com.markettwits.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.news_list.data.NewsDataSource
import com.markettwits.news_list.data.NewsDataSourceBase
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import org.koin.dsl.module

val newsModule = module(createdAtStart = true) {
    includes(sportSouceNetworkModule)
    single<NewsDataSource> {
        NewsDataSourceBase(
            service = get(),
            mapper = get()
        )
    }
    single<NewsRemoteToDomainMapper> {
        NewsRemoteToDomainMapper.Base(BaseTimeMapper())
    }
    single<NewsStoreFactory> {
        NewsStoreFactory(
            storeFactory = DefaultStoreFactory(),
            dataSource = get()
        )
    }
}

