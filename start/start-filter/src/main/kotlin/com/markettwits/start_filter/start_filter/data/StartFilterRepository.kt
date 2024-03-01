package com.markettwits.start_filter.start_filter.data

import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

internal interface StartFilterRepository {
    suspend fun filter(): Flow<Result<StartFilter>>
    suspend fun starts(state: StartFilterUi): Result<List<StartsListItem>>
}