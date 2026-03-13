package com.markettwits.sportsouce.edit_profile.sign_out.domain

interface SignOutUseCase {
    suspend fun signOut(): Result<Unit>
}