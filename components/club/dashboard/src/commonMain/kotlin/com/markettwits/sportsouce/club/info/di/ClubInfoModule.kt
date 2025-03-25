package com.markettwits.sportsouce.club.info.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.club.common.data.mapper.club_info.ClubInfoMapper
import com.markettwits.sportsouce.club.common.data.mapper.club_info.ClubInfoMapperBase
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponentBase
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

internal val clubInfoModule = module {
    includes(com.markettwits.sportsouce.club.cloud.di.clubCloudModule)
    singleOf(::ClubInfoStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ClubInfoMapperBase) bind ClubInfoMapper::class
}

internal fun Scope.createClubInfoComponent(
    componentContext: ComponentContext,
    goBack: () -> Unit,
    items: List<ClubInfo>,
    index: Int,
): ClubInfoComponent =
    ClubInfoComponentBase(
        componentContext = componentContext,
        storeFactory = get(),
        dismiss = goBack,
        items = items,
        index = index
    )