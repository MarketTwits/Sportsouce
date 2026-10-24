package com.markettwits.sportsouce.profile.results.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.results.domain.*

internal class ResultsActionsRepositoryBase(private val api: ResultsActionsApi, private val token: suspend () -> String) : ResultsActionsRepository {
    constructor(api: ResultsActionsApi, auth: AuthDataSource) : this(api, { auth.updateToken().getOrThrow() })
    override suspend fun starts(query: String) = api.starts(token(), query)
    override suspend fun search(query: String, startId: Int?) = api.search(token(), query, startId)
    override suspend fun claims() = api.claims(token())
    override suspend fun candidates(dismissed: Boolean) = api.candidates(token(), dismissed)
    override suspend fun suggestions() = api.suggestions(token())
    override suspend fun claim(id: Int) = api.claim(token(), id)
    override suspend fun candidate(id: Int, action: String) = api.candidate(token(), id, action)
    override suspend fun suggestion(id: Int, accept: Boolean) = api.suggestion(token(), id, accept)
    override suspend fun conflict(id: Int, transfer: Boolean) = api.conflict(token(), id, transfer)
    override suspend fun add(request: ExternalResultRequest) = api.add(token(), request)
    override suspend fun upload(file: ResultProofFile) = api.upload(token(), file)
}
