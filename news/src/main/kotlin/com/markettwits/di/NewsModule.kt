package com.markettwits.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.news_list.data.NewsDataSource
import com.markettwits.news_list.data.NewsDataSourceBase
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import org.koin.dsl.module
import com.markettwits.cloud.provider.HttpClientProvider
import org.koin.core.module.dsl.singleOf
import ru.alexpanov.core_network.provider.JsonProvider

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

