package com.markettwits.cloud.api

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.cloud.model.profile.ChangeProfileInfoResponse
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.cloud.model.start_comments.response.CommentRow
import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.start_user.RemouteStartsUserItem
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote


interface SportsouceApi {
    //Common
    suspend fun teams() : TeamsRemote
    suspend fun cities() : CityRemote
    //Starts
    suspend fun fetchActualStarts(): StartsRemote
    suspend fun fetchPasteStarts(): StartsRemote
    suspend fun fetchPreview(): StartsRemote
    suspend fun fetchStartMain(): StartsRemote

    //Start
    suspend fun fetchStart(startId: Int): StartRemote
    suspend fun fetchStartMember(startId: Int): List<StartMemberItem>
    suspend fun fetchStartComments(startId: Int): StartCommentsRemote
    suspend fun writeComment(startCommentRequest: StartCommentRequest, token: String): CommentRow
    suspend fun writeSubComment(
        startSubCommentRequest: StartSubCommentRequest,
        token: String
    ): Reply
    //Auth
    suspend fun signIn(signInRequest: SignInRequest): SignInResponseSuccess
    suspend fun auth(token: String): User
    //Profile
    suspend fun changeProfileInfo(profile: ChangeProfileInfoRequest, token: String) : ChangeProfileInfoResponse
    suspend fun changePassword(password : ChangePasswordRequest, token: String) : ChangeProfileInfoResponse
    suspend fun userRegistries(userId : Int, token : String) : List<RemouteStartsUserItem>
    //News
    suspend fun news() : NewsRemote
}