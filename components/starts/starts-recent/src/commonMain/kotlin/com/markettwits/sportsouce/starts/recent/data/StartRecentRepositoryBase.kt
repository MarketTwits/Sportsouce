package com.markettwits.sportsouce.starts.recent.data

import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.recent.domain.StartRecentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class StartRecentRepositoryBase(
    private val cache: StartsRecentCache,
) : StartRecentRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _recentStarts = MutableStateFlow<List<StartsListItem>>(emptyList())
    override val recentStarts: StateFlow<List<StartsListItem>> = _recentStarts.asStateFlow()

    init {
        scope.launch {
            _recentStarts.value = cache.getList()
        }
        scope.launch {
            cache.observe().collectLatest { starts ->
                _recentStarts.value = starts ?: emptyList()
            }
        }
    }

    override suspend fun add(start: StartsListItem) {
        cache.set(value = start)
    }

    override suspend fun clear(): Result<Unit> = runCatching {
        cache.clear()
    }
}
