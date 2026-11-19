package com.markettwits.sportsouce.starts.albums.di

import com.markettwits.sportsouce.starts.albums.filter.store.AlbumsFilterStoreFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.starts.albums.data.StartsAlbumsRepositoryBase
import com.markettwits.sportsouce.starts.albums.domain.StartsAlbumsRepository
import com.markettwits.sportsouce.starts.albums.presentation.store.StartsAlbumsStoreFactory
import com.markettwits.sportsouce.starts.cloud.di.sportSauceStartsCloudModule
import com.markettwits.sportsouce.start.presentation.album.di.startAlbumModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsAlbumsDataModule = module {
    includes(sportSauceStartsCloudModule)
    singleOf(::StartsAlbumsRepositoryBase) bind StartsAlbumsRepository::class
}

internal val startsAlbumsModule = module {
    includes(startsAlbumsDataModule, startAlbumModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsAlbumsStoreFactory)
    singleOf(::AlbumsFilterStoreFactory)
}
