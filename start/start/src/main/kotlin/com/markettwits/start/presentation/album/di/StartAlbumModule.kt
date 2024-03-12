package com.markettwits.start.presentation.album.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startAlbumModule = module {
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartAlbumStoreFactory)
}