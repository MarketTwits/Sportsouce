package com.markettwits.club.dashboard.di

import com.arkivanov.decompose.ComponentContext
import com.markettwits.bottom_bar.di.bottomBarModule
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.common.data.ClubRepositoryBase
import com.markettwits.club.common.data.mapper.SubscriptionMapperBase
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.common.domain.mapper.SubscriptionMapper
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponent
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponentBase
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStoreFactory
import com.markettwits.crashlitics.api.di.crashlyticsModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

val clubDashboardModule = module {
    includes(clubCloudModule, bottomBarModule, crashlyticsModule)
    singleOf(::ClubRepositoryBase) bind ClubRepository::class
    singleOf(::ClubDashboardStoreFactory)
    singleOf(::SubscriptionMapperBase) bind SubscriptionMapper::class
}

internal fun Scope.createDashboardComponent(
    componentContext: ComponentContext,
    output: (ClubDashboardComponent.Output) -> Unit,
): ClubDashboardComponent =
    ClubDashboardComponentBase(
        componentContext = componentContext,
        storeFactory = get(),
        listener = get(),
        output = output
    )

