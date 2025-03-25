package com.markettwits.sportsouce.start.data.start

import com.markettwits.cahce.InMemoryCache
import com.markettwits.sportsouce.start.domain.StartItem


internal class StartMemoryCache : InMemoryCache<Result<StartItem>>()