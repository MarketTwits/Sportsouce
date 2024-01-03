package com.markettwits.change_password.data

import kotlinx.coroutines.flow.Flow

interface ChangePasswordDataSource {
    val state : Flow<String>
    suspend fun changePassword(password : String, newPassword : String)
    suspend fun changePasswordNew(password: String, newPassword: String) : Result<String>
}