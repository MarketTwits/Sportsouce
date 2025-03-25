package com.markettwits.sportsouce.start.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule
import com.markettwits.sportsouce.start.data.start.StartMemoryCache
import com.markettwits.sportsouce.start.data.start.StartRepositoryBase
import com.markettwits.sportsouce.start.data.start.mapper.albums.StartAlbumsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.albums.StartAlbumsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.comments.StartCommentsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.comments.StartCommentsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.result.StartMembersResultsToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.result.StartMembersResultsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.start.StartRemoteToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.time.StartTimesMapper
import com.markettwits.sportsouce.start.data.start.mapper.time.StartTimesMapperBase
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStoreFactory
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startModule = module {
    includes(
        sportSauceStartNetworkModule,
        startsCommonModule,
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
    singleOf(::StartRemoteToUiMapperBase) bind StartRemoteToUiMapper::class
    singleOf(::StartTimesMapperBase) bind StartTimesMapper::class
    singleOf(::StartMembersResultsToUiMapperBase) bind StartMembersResultsToUiMapper::class
    singleOf(::StartMemberResultsStoreFactory)
    singleOf(::StartMemoryCache)
}
