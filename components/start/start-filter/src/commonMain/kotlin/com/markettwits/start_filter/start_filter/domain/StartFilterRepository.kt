package com.markettwits.start_filter.start_filter.domain

import com.markettwits.start_filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

internal interface StartFilterRepository {

    suspend fun filter(): Flow<StartFilter>

    suspend fun starts(
        state: StartFilterUi,
        sorted: StartFilter.Sorted
    ): Result<List<StartsListItem>>

}