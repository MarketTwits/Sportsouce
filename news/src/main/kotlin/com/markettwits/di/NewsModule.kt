package com.markettwits.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.news_list.data.NewsDataSource
import com.markettwits.news_list.data.NewsDataSourceBase
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import org.koin.dsl.module
import com.markettwits.cloud.provider.HttpClientProvider
import ru.alexpanov.core_network.provider.JsonProvider

val newsModule = module(createdAtStart = true) {
    single<NewsDataSource> {
        NewsDataSourceBase(
            StartsRemoteDataSourceImpl(
                HttpClientProvider(
                    JsonProvider().get(),
                    "https://sport-73zoq.ondigitalocean.app"
                )
            ),
            NewsRemoteToDomainMapper.Base(BaseTimeMapper())
        )
    }
    single<NewsStoreFactory> {
        NewsStoreFactory(
            storeFactory = DefaultStoreFactory(),
            dataSource = get()
        )
    }
    }

