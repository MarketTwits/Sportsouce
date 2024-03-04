package com.markettwits.cloud.api

import com.markettwits.cloud.model.auth.reset_password.ResetPasswordResponse
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.image.UploadFileResponse
import com.markettwits.cloud.model.kind_of_sport.KindOfSportRemote
import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.cloud.model.profile.members.ProfileMemberRequest
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoResponse
import com.markettwits.cloud.model.promocode.PromocodeRemote
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.cloud.model.sign_up.SignUpResponse
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.cloud.model.start_comments.response.CommentRow
import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponseWithoutPayment
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote
import java.io.File


interface SportsouceApi {
    //Common
    suspend fun teams() : TeamsRemote
    suspend fun cities(withStarts : Boolean = false) : CityRemote
    suspend fun kindOfSports() : KindOfSportRemote
    suspend fun seasons() : StartSeasonsRemote
    suspend fun startWithFilter(request : Map<String, String>) : StartsRemote
    //Starts
    suspend fun fetchActualStarts(): StartsRemote
    suspend fun fetchPasteStarts(): StartsRemote
    suspend fun fetchPreview(): StartsRemote
    suspend fun fetchStartMain(): StartsRemote
    //Start registry
    suspend fun repay(id : Int, token: String) : StartRegistrationResponse
    suspend fun registerOnStartBase(
        request: StartRegisterRequest,
        token: String
    ): StartRegistrationResponse

    suspend fun registerOnStartCombo(
        request: StartRegisterRequest.Combo,
        token: String
    ): StartRegistrationResponse
    suspend fun registerOnStartWithoutPayment(request: StartRegisterRequest,token : String) : StartRegistrationResponseWithoutPayment
    suspend fun promo(value : String,startId: Int) : PromocodeRemote
    //Start
    suspend fun fetchStart(startId: Int): StartRemote
    suspend fun fetchStartMember(startId: Int): List<StartMemberItem>
    suspend fun fetchStartComments(startId: Int): StartCommentsRemote
    suspend fun writeComment(startCommentRequest: StartCommentRequest, token: String): CommentRow
    suspend fun writeSubComment(subComment: StartSubCommentRequest, token: String): Reply
    //Auth
    suspend fun resetPassword(email: String): ResetPasswordResponse
    suspend fun register(signUpRequest: SignUpRequest) : SignUpResponse
    suspend fun signIn(signInRequest: SignInRequest): SignInResponseSuccess
    suspend fun auth(token: String): User
    //Profile
    suspend fun uploadFile(file: File): UploadFileResponse
    suspend fun changeProfileInfo(profile: ChangeProfileInfoRequest, token: String) : ChangeProfileInfoResponse
    suspend fun changePassword(password : ChangePasswordRequest, token: String) : ChangeProfileInfoResponse
    suspend fun userRegistries(userId: Int, token: String): List<RemoteStartsUserItem>

    //Member
    suspend fun memberTemplate(userId: Int, token: String): ProfileMembers
    suspend fun deleteMember(memberId: Int, token: String)
    suspend fun updateMember(memberId: Int, request: ProfileMemberRequest, token: String)
    suspend fun addMember(request: ProfileMemberRequest, token: String)
    //News
    suspend fun news() : NewsRemote
}