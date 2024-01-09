package com.markettwits.start.data

import com.markettwits.core_ui.cache.Cache
import com.markettwits.core_ui.cache.MemoryCache
import com.markettwits.start.presentation.start.StartItemUi

class StartMemoryCache : Cache<StartItemUi> by MemoryCache()