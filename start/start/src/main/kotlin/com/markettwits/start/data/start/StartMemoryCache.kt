package com.markettwits.start.data.start

import com.markettwits.cahce.InMemoryCache
import com.markettwits.start.domain.StartItem


internal class StartMemoryCache : InMemoryCache<Result<StartItem>>()