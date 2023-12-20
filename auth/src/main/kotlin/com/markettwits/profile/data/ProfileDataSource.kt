package com.markettwits.profile.data

interface AuthDataSource {
    suspend fun auth(email : String, password : String)
    suspend fun show() : String
}