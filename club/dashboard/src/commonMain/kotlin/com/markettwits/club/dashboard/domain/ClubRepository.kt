package com.markettwits.club.dashboard.domain

import com.markettwits.club.dashboard.domain.model.SubscriptionItems
import kotlinx.coroutines.flow.Flow

interface ClubRepository {
    suspend fun subscriptions(): Flow<List<SubscriptionItems>>
}