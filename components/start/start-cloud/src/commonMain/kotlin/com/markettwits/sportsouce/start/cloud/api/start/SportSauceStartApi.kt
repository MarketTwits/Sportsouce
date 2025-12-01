package com.markettwits.sportsouce.start.cloud.api.start

import com.markettwits.sportsouce.start.cloud.model.comments.request.StartCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.comments.response.Reply
import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationRequest
import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationResponse
import com.markettwits.sportsouce.start.cloud.model.filters.FiltersRemote
import com.markettwits.sportsouce.start.cloud.model.kindofsport.KindOfSportRemote
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.cloud.model.members.StartMembersRemote
import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultV1
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMembersResultRowsV2
import com.markettwits.sportsouce.start.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum


interface SportSauceStartApi {

    suspend fun start(startId: String): StartRemote

    suspend fun kindOfSports() : KindOfSportRemote

    suspend fun seasons() : StartSeasonsRemote

    suspend fun filters(startId: Int): FiltersRemote

    @Deprecated("Use membersFiltered(startId, ...) with server-side filtering and sorting")
    suspend fun members(startId: Int): List<StartMember>

    suspend fun membersFiltered(
        startId: Int,
        filterText: String = "",
        skipCount: Int = 0,
        maxResultCount: Int = 20,
        distances: List<Int> = emptyList(),
        genders: List<String> = emptyList(),
        sorting: String? = null,
    ): StartMembersRemote

    suspend fun membersResults(
        maxResultCount: Int,
        startId: Int,
        filterText: String = "",
        sorting: String = "result asc",
    ): List<StartMemberResultV1>

    suspend fun membersResultsAnalyze(
        startId: Int,
        page: Int = 1,
        maxResultCount: Int = 10,
        gender: String = "",
        group: String = "",
        distance: String = "",
        searchQuery: String = "",
    ): StartMembersResultRowsV2

    suspend fun donation(startDonationRequest: StartDonationRequest): StartDonationResponse

    suspend fun albums(startId: Int): List<StartAlbum>

    suspend fun comments(startId: Int): List<Comment>

    suspend fun writeComment(startCommentRequest: StartCommentRequest, token: String): Comment

    suspend fun writeSubComment(subComment: StartSubCommentRequest, token: String): Reply
}