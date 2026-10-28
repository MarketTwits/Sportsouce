package com.markettwits.sportsouce.profile

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.auth.cloud.model.sign_up.SignUpRequest
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.registrations.domain.*
import kotlinx.coroutines.flow.Flow

internal fun profileRegistration(id: Int) = StartOrderInfo(id, 300, "10 км", "", "28 октября 2026", "", "",
    StartOrderPaymentStatus.Success(), emptyList(), StartOrderType.Default, "Осенний забег", "500")

internal fun profileSnapshot(id: Int = 1, registrationIds: List<Int> = listOf(10)) = UserProfile(id,
    UserProfile.SocialNetwork("", "", "", ""), UserProfile.Activity(registrationIds.map(::profileRegistration), 2),
    UserProfile.UserInfo("Иван", "Иванов", "", "", "", ""))

internal class ProfileAuth : AuthDataSource {
    var current = User(id = 1, name = "Иван", surname = "Иванов", number = "", address = null, age = null,
        birthday = "2000-01-01", commentForAddress = null, createdAt = "2026-01-01T00:00:00Z", instagram = null,
        patronymic = null, photoId = null, sex = "Мужской", team = null, telegram = null, vk = null, whatsapp = null)
    var authCalls = 0
    var onAuth: suspend () -> Unit = {}
    override suspend fun userID() = Result.success(current.id)
    override suspend fun auth(): Result<User> { authCalls++; onAuth(); return Result.success(current) }
    override suspend fun updateToken() = Result.success("test-token")
    override suspend fun user() = Result.success(current)
    override suspend fun observeUser(): Flow<User> = error("Profile must not subscribe to its own auth cache writes")
    override suspend fun sharedUser(): Result<SharedUser> = error("Unexpected sharedUser")
    override suspend fun register(signUpRequest: SignUpRequest): Result<Unit> = error("Unexpected register")
    override suspend fun logIn(emailOrPhone: String, password: String): Result<User> = error("Unexpected login")
    override suspend fun updatePassword(password: String) = error("Unexpected password change")
    override suspend fun updateUser(request: ChangeProfileInfoRequest): Result<Unit> = error("Unexpected profile change")
    override suspend fun clear() = error("Unexpected clear")
}
