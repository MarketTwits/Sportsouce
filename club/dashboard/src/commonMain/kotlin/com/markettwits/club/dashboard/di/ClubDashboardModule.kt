package com.markettwits.club.dashboard.di

import com.markettwits.bottom_bar.component.storage.BottomBarStorageImpl
import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.dashboard.data.ClubRepositoryBase
import com.markettwits.club.dashboard.data.SubscriptionMapperBase
import com.markettwits.club.dashboard.data.cache.ClubSubscriptionsCache
import com.markettwits.club.dashboard.domain.ClubRepository
import com.markettwits.club.dashboard.domain.SubscriptionMapper
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
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    single<ObservableCache<List<SubscriptionItemsRemote>>> {
        ClubSubscriptionsCache()
    }
}

internal fun Scope.createDashboardComponent(
    goBack: () -> Unit,
    goInfo: (Int) -> Unit,
): ClubDashboardComponent =
    ClubDashboardComponentBase(
        componentContext = injectComponentContext,
        storeFactory = get(),
        goBack = goBack,
        goInfo = goInfo,
        listener = BottomBarStorageImpl
    )

