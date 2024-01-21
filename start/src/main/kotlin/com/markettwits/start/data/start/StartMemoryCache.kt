package com.markettwits.start.data.start

import com.markettwits.core_ui.cache.Cache
import com.markettwits.core_ui.cache.MemoryCache
import com.markettwits.start.domain.StartItem

class StartMemoryCache : Cache<Result<StartItem>> by MemoryCache()