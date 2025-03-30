package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain

interface SignOutUseCase {
    suspend fun signOut(): Result<Unit>
}