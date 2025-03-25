package com.markettwits.sportsouce.start.search.filter.domain

import kotlinx.coroutines.flow.Flow

internal interface StartFilterRepository {
    suspend fun filter(): Flow<StartFilter>
}