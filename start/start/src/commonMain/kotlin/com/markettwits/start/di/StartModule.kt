package com.markettwits.start.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.start.data.start.StartMemoryCache
import com.markettwits.start.data.start.StartRepositoryBase
import com.markettwits.start.data.start.mapper.albums.StartAlbumsToUiMapper
import com.markettwits.start.data.start.mapper.albums.StartAlbumsToUiMapperBase
import com.markettwits.start.data.start.mapper.comments.StartCommentsToUiMapper
import com.markettwits.start.data.start.mapper.comments.StartCommentsToUiMapperBase
import com.markettwits.start.data.start.mapper.distance.StartDistancesToUiMapper
import com.markettwits.start.data.start.mapper.distance.StartDistancesToUiMapperBase
import com.markettwits.start.data.start.mapper.members.StartMembersToUiMapper
import com.markettwits.start.data.start.mapper.members.StartMembersToUiMapperBase
import com.markettwits.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.start.data.start.mapper.start.StartRemoteToUiMapperBase
import com.markettwits.start.data.start.mapper.time.StartTimesMapper
import com.markettwits.start.data.start.mapper.time.StartTimesMapperBase
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.start.store.StartScreenStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startModule = module {
    includes(
        sportSouceNetworkModule,
        timeApiNetworkModule,
        authDataSourceModule,
        crashlyticsModule,
        intentActionModule
    )
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartScreenStoreFactory)
    singleOf(::StartRepositoryBase) bind StartRepository::class
    singleOf(::StartAlbumsToUiMapperBase) bind StartAlbumsToUiMapper::class
    singleOf(::StartMembersToUiMapperBase) bind StartMembersToUiMapper::class
    singleOf(::StartCommentsToUiMapperBase) bind StartCommentsToUiMapper::class
    singleOf(::StartDistancesToUiMapperBase) bind StartDistancesToUiMapper::class
    singleOf(::StartRemoteToUiMapperBase) bind StartRemoteToUiMapper::class
    singleOf(::StartTimesMapperBase) bind StartTimesMapper::class
    singleOf(::StartMemoryCache)
}
