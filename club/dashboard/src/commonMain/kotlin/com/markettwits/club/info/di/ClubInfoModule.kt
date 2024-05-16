package com.markettwits.club.info.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponent
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponentBase
import com.markettwits.club.info.data.ClubInfoMapperBase
import com.markettwits.club.info.data.ClubInfoRepositoryBase
import com.markettwits.club.info.domain.ClubInfoMapper
import com.markettwits.club.info.domain.ClubInfoRepository
import com.markettwits.club.info.presentation.component.ClubInfoComponent
import com.markettwits.club.info.presentation.component.ClubInfoComponentBase
import com.markettwits.club.info.presentation.store.ClubInfoStoreFactory
import com.markettwits.injectComponentContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

internal val clubInfoModule = module {
    includes(clubCloudModule)
    singleOf(::ClubInfoStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ClubInfoRepositoryBase) bind ClubInfoRepository::class
    singleOf(::ClubInfoMapperBase) bind ClubInfoMapper::class
}

internal fun Scope.createClubInfoComponent(
    goBack: () -> Unit,
    index: Int,
): ClubInfoComponent =
    ClubInfoComponentBase(
        componentContext = injectComponentContext,
        storeFactory = get(),
        dismiss = goBack,
        index = index
    )