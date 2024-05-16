package com.markettwits.club.dashboard.data

import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.club.cloud.api.SportSauceClubsApi
import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.dashboard.domain.ClubRepository
import com.markettwits.club.dashboard.domain.model.SubscriptionItems
import com.markettwits.club.dashboard.domain.SubscriptionMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClubRepositoryBase(
    private val service: SportSauceClubsApi,
    private val cacheExecutor: ExecuteWithCache,
    private val cache: ObservableCache<List<SubscriptionItemsRemote>>,
    private val subscriptionMapper: SubscriptionMapper
) : ClubRepository {
    //    override suspend fun subscriptions(): Flow<List<SubscriptionItemsRemote>> =
//        cacheExecutor.executeWithCache(
//            forced = false,
//            cache = cache,
//            launch = { service.subscription() },
//        )
    override suspend fun subscriptions(): Flow<List<SubscriptionItems>> = flow {
        val subscriptions = subscriptionMapper.map(service.subscription())
        emit(subscriptions)
    }

}