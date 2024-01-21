package com.markettwits.start.data.start

import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.core_ui.base.Fourth
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.CommentUiState
import com.markettwits.start.presentation.start.StartItemUi
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import com.markettwits.cloud.api.SportsouceApi

class BaseStartDataSource(
    private val service: SportsouceApi,
    private val timeService: TimeApi,
    private val authService: AuthDataSource,
    private val mapper: StartRemoteToUiMapper,
    private val cache: StartMemoryCache
) : StartDataSource {
    override suspend fun start(startId: Int, relaunch: Boolean): StartItemUi {
        return if (relaunch)
            return launch(startId)
        else cache[startId] ?: launch(startId)
    }

    private suspend fun launch(startId: Int): StartItemUi {
        return try {
            val (data, withFilter, comments, time) = coroutineScope {
                withContext(Dispatchers.IO) {
                    val deferredTime = async { timeService.currentTime() }
                    val deferredData = async { service.fetchStart(startId) }
                    val deferredWithFilter = async { service.fetchStartMember(startId) }
                    val deferredComments = async { service.fetchStartComments(startId) }
                    Fourth(
                        deferredData.await(),
                        deferredWithFilter.await(),
                        deferredComments.await(),
                        deferredTime.await()
                    )
                }
            }
            val result = mapper.map(data, withFilter, comments, time)
            cache.set(value = result, key = startId)
            result
        } catch (e: Exception) {
            mapper.map(e)
        }
    }


    override suspend fun startMember(startId: Int): List<StartMembersUi> {
        val startMember = service.fetchStartMember(startId)
        return mapper.map(startMember)
    }

    override suspend fun writeComment(comment: String, startId: Int): CommentUiState {
        return try {
            val token = authService.updateToken()
            val user = authService.auth()
            service.writeComment(
                startCommentRequest = StartCommentRequest(comment, startId, user.id.toString()),
                token = token
            )
            CommentUiState.Success
        } catch (e: Exception) {
            when (e) {
                is AuthException -> CommentUiState.Error(e.exception)
                is ClientRequestException -> CommentUiState.Error(e.response.body<AuthErrorResponse>().message)
                else -> CommentUiState.Error(e.message.toString())
            }
        }
    }

    override suspend fun writeSubComment(comment: String, parentCommentId: Int): CommentUiState {
        return try {
            val token = authService.updateToken()
            val user = authService.auth()
            service.writeSubComment(
                startSubCommentRequest = StartSubCommentRequest(
                    comment,
                    parentCommentId,
                    user.id.toString()
                ),
                token = token
            )
            CommentUiState.Success
        } catch (e: Exception) {
            when (e) {
                is AuthException -> CommentUiState.Error(e.exception)
                is ClientRequestException -> CommentUiState.Error(e.response.body<AuthErrorResponse>().message)
                else -> CommentUiState.Error(e.message.toString())
            }
        }
    }
}