package com.markettwits.sportsouce.profile.results.domain

interface ResultsActionsRepository {
    suspend fun starts(query: String): List<StartSuggestion>
    suspend fun search(query: String, startId: Int?): List<UnmatchedResult>
    suspend fun claims(): List<ResultClaim>
    suspend fun candidates(dismissed: Boolean = false): List<ResultCandidate>
    suspend fun suggestions(): List<ResultClaim>
    suspend fun claim(id: Int)
    suspend fun candidate(id: Int, action: String)
    suspend fun suggestion(id: Int, accept: Boolean)
    suspend fun conflict(id: Int, transfer: Boolean)
    suspend fun add(request: ExternalResultRequest)
    suspend fun upload(file: ResultProofFile): Int
}
