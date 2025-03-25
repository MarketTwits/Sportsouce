package com.markettwits.sportsouce.edit_profile.edit_profile_change_password.data

interface ChangePasswordDataSource {
    suspend fun changePassword(password: String, newPassword: String): Result<String>
}