package com.markettwits.change_password.data

interface ChangePasswordDataSource {
    suspend fun changePassword(password: String, newPassword: String) : Result<String>
}