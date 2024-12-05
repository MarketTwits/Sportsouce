package com.markettwits.start_cloud.api.start

import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.start_cloud.model.comments.request.StartCommentRequest
import com.markettwits.start_cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.filters.FiltersRemote
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum


interface SportSauceStartApi {

    suspend fun start(startId : Int) : StartRemote

    suspend fun filters(startId: Int) : FiltersRemote

    suspend fun membersNew(startId: Int) : List<StartMember>

    suspend fun albums(startId: Int) : List<StartAlbum>

    suspend fun comments(startId : Int) : List<Comment>

    suspend fun writeComment(startCommentRequest: StartCommentRequest, token: String): Comment

    suspend fun writeSubComment(subComment: StartSubCommentRequest, token: String): Reply

}