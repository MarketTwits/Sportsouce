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
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.cloud.model.sign_up.SignUpResponse
import com.markettwits.cloud.model.start_donation.StartDonationRequest
import com.markettwits.cloud.model.start_donation.StartDonationResponse
import com.markettwits.cloud.model.start_price.StartPriceRequest
import com.markettwits.cloud.model.start_price.StartPriceResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote


interface SportsouceApi {
    //Common
    suspend fun teams() : TeamsRemote

    suspend fun cities(withStarts : Boolean = false) : CityRemote

    suspend fun kindOfSports() : KindOfSportRemote

    suspend fun seasons() : StartSeasonsRemote

    //Starts
    suspend fun startWithFilter(request : Map<String, String>) : StartsRemote

    suspend fun fetchActualStarts(): StartsRemote

    suspend fun fetchPasteStarts(): StartsRemote

    suspend fun fetchPreview(): StartsRemote

    suspend fun fetchStartMain(): StartsRemote
    //Start registry
    suspend fun repay(id : Int, token: String) : StartRegistrationResponse

    suspend fun checkStartPrice(
        startPriceRequest: StartPriceRequest,
        id : Int,
        token: String
    ) : StartPriceResponse

    suspend fun donation(startDonationRequest: StartDonationRequest): StartDonationResponse
    //Auth
    suspend fun resetPassword(email: String): ResetPasswordResponse

    suspend fun register(signUpRequest: SignUpRequest) : SignUpResponse

    suspend fun signIn(signInRequest: SignInRequest): SignInResponseSuccess

    suspend fun auth(token: String): User
    //Profile
    //suspend fun uploadFile(file: File): UploadFileResponse

    suspend fun changeProfileInfo(profile: ChangeProfileInfoRequest, token: String) : ChangeProfileInfoResponse

    suspend fun changePassword(password : ChangePasswordRequest, token: String) : ChangeProfileInfoResponse

    suspend fun userRegistriesNew2(userId: Int, token : String) : List<UserRegistration>
    //Member
    suspend fun memberTemplate(userId: Int, token: String): ProfileMembers

    suspend fun deleteMember(memberId: Int, token: String)

    suspend fun updateMember(memberId: Int, request: ProfileMemberRequest, token: String)

    suspend fun addMember(request: ProfileMemberRequest, token: String)
    //News
    suspend fun news() : NewsRemote
}