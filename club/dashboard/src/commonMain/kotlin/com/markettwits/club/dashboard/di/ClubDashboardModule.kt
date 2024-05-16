package com.markettwits.club.dashboard.di

import com.markettwits.bottom_bar.component.storage.BottomBarStorageImpl
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.common.data.ClubRepositoryBase
import com.markettwits.club.common.data.mapper.SubscriptionMapperBase
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.common.domain.mapper.SubscriptionMapper
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponent
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponentBase
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStoreFactory
import com.markettwits.injectComponentContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

val clubDashboardModule = module {
    includes(clubCloudModule)
    singleOf(::ClubRepositoryBase) bind ClubRepository::class
    singleOf(::ClubDashboardStoreFactory)
    singleOf(::SubscriptionMapperBase) bind SubscriptionMapper::class
}

internal fun Scope.createDashboardComponent(
    output: (ClubDashboardComponent.Output) -> Unit,
): ClubDashboardComponent =
    ClubDashboardComponentBase(
        componentContext = injectComponentContext,
        storeFactory = get(),
        listener = BottomBarStorageImpl,
        output = output
    )

