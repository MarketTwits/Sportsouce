package com.markettwits.sportsouce.news.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.news.common.newsCommonModule
import com.markettwits.sportsouce.news.news_list.store.NewsStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val newsModule = module(createdAtStart = true) {
    includes(newsCommonModule)
    singleOf(::NewsStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}

