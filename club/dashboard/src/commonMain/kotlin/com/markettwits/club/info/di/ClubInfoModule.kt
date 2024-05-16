package com.markettwits.club.info.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.common.data.mapper.ClubInfoMapperBase
import com.markettwits.club.common.domain.mapper.ClubInfoMapper
import com.markettwits.club.info.domain.models.ClubInfo
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
    singleOf(::ClubInfoMapperBase) bind ClubInfoMapper::class
}

internal fun Scope.createClubInfoComponent(
    goBack: () -> Unit,
    items: List<ClubInfo>,
    index: Int,
): ClubInfoComponent =
    ClubInfoComponentBase(
        componentContext = injectComponentContext,
        storeFactory = get(),
        dismiss = goBack,
        items = items,
        index = index
    )