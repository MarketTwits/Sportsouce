package com.markettwits.sportsouce.club.dashboard.di

import com.arkivanov.decompose.ComponentContext
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.sportsouce.club.common.data.ClubRepositoryBase
import com.markettwits.sportsouce.club.common.data.mapper.subscription.SubscriptionMapper
import com.markettwits.sportsouce.club.common.data.mapper.subscription.SubscriptionMapperBase
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponentBase
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

val clubDashboardModule = module {
    includes(com.markettwits.sportsouce.club.cloud.di.clubCloudModule, crashlyticsModule)
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
        output = output
    )

