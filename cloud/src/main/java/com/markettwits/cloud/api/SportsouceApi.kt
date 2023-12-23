package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponse
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.starts.StartsRemote

interface SportsouceApi {
    //Starts
    suspend fun fetchActualStarts() : StartsRemote
    suspend fun fetchPasteStarts() : StartsRemote
    suspend fun fetchPreview() : StartsRemote
    //Start
    suspend fun fetchStart(startId : Int) : StartRemote
    suspend fun fetchStartMember(startId: Int) : List<StartMemberItem>
    suspend fun fetchStartComments(startId: Int) : StartCommentsRemote
    //Auth
    suspend fun signIn(signInRequest: SignInRequest) : SignInResponseSuccess
    suspend fun auth(token : String) : User
}