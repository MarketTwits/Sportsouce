package com.markettwits.sportsouce.start.data.start

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.map
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.core.log.infoLog
import com.markettwits.core_ui.items.extensions.fetchFifth
import com.markettwits.core_ui.items.extensions.retryRunCatchingAsync
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.StartRemoteNew
import com.markettwits.sportsouce.start.cloud.model.start.StartRemoteOld
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersNewToUiMapper
import com.markettwits.sportsouce.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.sportsouce.start.domain.*
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.markettwits.sportsouce.start.cloud.model.filters.FiltersRemote as CloudFiltersRemote

internal class StartRepositoryBase(
    private val startService: SportSauceStartApi,
    private val authService: AuthDataSource,
    private val startMapper: StartRemoteToUiMapper,
    private val startsService: SportSauceStartsApi,
    private val cache: StartMemoryCache
) : StartRepository, LogTagProvider {

    override val tag: String = "StartRepository"

    // Helper extension to extract ID from StartRemote implementations
    private val StartRemote.id: Int
        get() = when (this) {
            is StartRemoteNew -> this.id
            is StartRemoteOld -> this.startData.id
        }

    override suspend fun start(
        startId: String,
        relaunch: Boolean
    ): Result<StartItem> {
        val numericId = startId.toIntOrNull()

        return if (numericId != null) {
            if (relaunch) {
                launches(startId)
            } else {
                retryRunCatchingAsync(times = 2, interval = 2000L) {
                    cache.get(numericId)
                }.getOrNull() ?: launches(startId)
            }
        } else {
            launches(startId)
        }
    }

    override suspend fun startMembersResult(startId: Int, maxResultCount: Int): List<MemberResult> {
        return coroutineScope {
            val firstDeferred = async {
                runCatching {
                    startService.membersResults(startId = startId, maxResultCount = maxResultCount)
                }.onFailure {
                    errorLog { "Fail to fetch start members result ${it.message}" }
                }.onSuccess {
                    if (it.isEmpty()) {
                        errorLog { "Start members result is empty first" }
                    } else {
                        infoLog { "Start members result is not empty" }
                    }
                }
            }
            val secondDeferred = async {
                runCatching {
                    startService.membersResultsAnalyze(startId = startId, maxResultCount = 1000)
                }.onFailure {
                    errorLog { "Fail to fetch start members result ${it.message}" }
                }.onSuccess {
                    if (it.rows.isEmpty()) {
                        errorLog { "Start members result is empty" }
                    } else {
                        infoLog { "Start members result is not empty second" }
                        errorLog { it.rows.toString() }
                    }
                }
            }
            val firstResult = firstDeferred.await()
            val secondResult = secondDeferred.await()
            return@coroutineScope when {
                firstResult.isSuccess && firstResult.getOrThrow().isNotEmpty() -> {
                    startMapper.mapR1(firstResult.getOrThrow())
                }

                secondResult.isSuccess && secondResult.getOrThrow().rows.isNotEmpty() -> {
                    startMapper.mapR2(secondResult.getOrThrow().rows)
                }

                firstResult.isSuccess -> {
                    startMapper.mapR1(firstResult.getOrThrow())
                }

                secondResult.isSuccess -> {
                    startMapper.mapR2(secondResult.getOrThrow().rows)
                }

                else -> emptyList()
            }
        }
    }


    override suspend fun startComments(startId: Int): Result<StartItem.Comments> =
        retryRunCatchingAsync {
            val value = startService.comments(startId)
            startMapper.map(value)
        }

    override suspend fun startsRecommended(startId: String): Result<List<StartsListItem>> =
        runCatching {
            // Check if startId is numeric or slug and extract numeric ID for filtering
            val numericStartId = startId.toIntOrNull() ?: run {
                // startId is slug, resolve to ID by calling the API
                val startData = startService.start(startId)
                startData.id
            }
            startsService.fetchActualStarts()
                .filter { it.id != numericStartId }
                .shuffled()
        }

    private suspend fun launches(startId: String): Result<StartItem> {
        val result = runCatching {
            val startData = startService.start(startId)
            val actualStartId = startData.id
            
            val cloud =
                fetchFifth<StartRemote, List<StartMember>, List<StartAlbum>, List<Comment>, List<MemberResult>>(
                    { startData }, // Use already fetched start data
                    { safeCallStartMembers(actualStartId) },
                    { startService.albums(actualStartId) },
                    { startService.comments(actualStartId) },
                    { startMembersResult(actualStartId, 10000) }
                )
            val result =
                startMapper.map(
                    cloud.first,
                    cloud.second,
                    cloud.fifth,
                    cloud.third,
                    cloud.fourth
                )

            errorLog { "StartItem mapped with ${result.membersResults.size} member results" }
            infoLog { "Start Item :${result.membersResults}" }
            // Cache using the actual ID
            cache.set(value = Result.success(result), key = actualStartId)
            result
        }
        return result
    }

    override suspend fun writeComment(
        startId: Int,
        comment: String,
        id: Int,
        subComment: Boolean
    ): CommentUiState {
        return try {
            val token = authService.updateToken().getOrThrow()
            val userId = authService.auth().getOrThrow().id
            if (subComment)
                startService.writeSubComment(
                    subComment = StartSubCommentRequest(
                        comment = comment,
                        parentCommentId = id,
                        personId = userId.toString(),
                        startId = startId
                    ),
                    token = token
                )
            else
                startService.writeComment(
                    startCommentRequest = StartCommentRequest(
                        comment = comment,
                        startId = id,
                        personId = userId.toString()
                    ),
                    token = token
                )
            CommentUiState.Success
        } catch (e: Exception) {
            CommentUiState.Error(e.networkExceptionHandler().message.toString())
        }
    }

    private suspend fun safeCallStartMembers(startId: Int): List<StartMember> {
        return kotlin.runCatching {
            startService.members(startId)
        }.fold(onSuccess = { it }
        ) {
            errorLog(it) { "Fail to launch startMembers start id $startId" }
            errorLog { "Error ${it.message}" }
            emptyList()
        }
    }

    override suspend fun membersFilters(startId: Int): Result<FiltersRemote> =
        retryRunCatchingAsync {
            val cloudFilters = startService.filters(startId)
            mapToDomainFilters(cloudFilters)
        }

    override fun pagingMembers(
        startId: Int,
        params: StartMembersPagingParams,
    ): Flow<PagingData<Pair<StartMembersUi, Int>>> {
        val pagingConfig = PagingConfig(
            pageSize = START_MEMBERS_ITEMS_PAGE_SIZE,
            initialLoadSize = START_MEMBERS_ITEMS_PAGE_SIZE,
            prefetchDistance = START_MEMBERS_ITEMS_PAGE_SIZE / 2,
            enablePlaceholders = false,
            maxSize = Int.MAX_VALUE,
            jumpThreshold = Int.MIN_VALUE
        )
        val pagingSource = StartMembersPagingSource(
            startId = startId,
            startNetworkApi = startService,
            params = params
        )
        val pager: Pager<Int, StartMember> = run {
            Pager(pagingConfig, null) { pagingSource }
        }
        return pager.flow.map { pagingData ->
            pagingData.map { item ->
                val item = StartMembersNewToUiMapper().map(item)
                Pair(item, pagingSource.getTotalCount())
            }
        }
    }

    private fun mapToDomainFilters(cloudFilters: CloudFiltersRemote): FiltersRemote {
        return FiltersRemote(
            cities = cloudFilters.cities.map { FilterItem(it.value, it.count) },
            distances = cloudFilters.distances.map { FilterItem(it.value, it.count, it.id) },
            genders = cloudFilters.genders.map { FilterItem(it.value, it.count) },
            groups = cloudFilters.groups.map { FilterItem(it.value, it.count, it.id) },
            teams = cloudFilters.teams.map { FilterItem(it.value, it.count) }
        )
    }
}