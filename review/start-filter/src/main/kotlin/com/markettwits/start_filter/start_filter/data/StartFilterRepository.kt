package com.markettwits.start_filter.start_filter.data

import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.starts.presentation.StartsListItem

internal interface StartFilterRepository {
    suspend fun filter() : Result<StartFilter>
    suspend fun starts(state : StartFilterUi) : Result<List<StartsListItem>>
}