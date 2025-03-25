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
import com.markettwits.sportsouce.start.cloud.model.result.StartMemberResult
import com.markettwits.sportsouce.start.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum


interface SportSauceStartApi {

    suspend fun start(startId: Int): StartRemote

    suspend fun kindOfSports() : KindOfSportRemote

    suspend fun seasons() : StartSeasonsRemote

    suspend fun filters(startId: Int): FiltersRemote

    suspend fun members(startId: Int): List<StartMember>

    suspend fun membersResults(
        maxResultCount: Int,
        startId: Int,
        filterText: String = "",
        sorting: String = "result asc"
    ): List<StartMemberResult>

    suspend fun donation(startDonationRequest: StartDonationRequest): StartDonationResponse

    suspend fun albums(startId: Int): List<StartAlbum>

    suspend fun comments(startId: Int): List<Comment>

    suspend fun writeComment(startCommentRequest: StartCommentRequest, token: String): Comment

    suspend fun writeSubComment(subComment: StartSubCommentRequest, token: String): Reply

}