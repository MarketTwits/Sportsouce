package com.markettwits.sportsouce.start.presentation.album.di

import com.markettwits.sportsouce.start.presentation.album.data.StartAlbumRepositoryBase
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumRepository
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startAlbumModule = module {
    includes(com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule, com.markettwits.intentActionModule)
    singleOf(::StartAlbumRepositoryBase) bind StartAlbumRepository::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartAlbumStoreFactory)
}
