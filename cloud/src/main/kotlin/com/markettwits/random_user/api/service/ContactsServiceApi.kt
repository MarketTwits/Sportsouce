package com.markettwits.random_user.api.service

import com.markettwits.random_user.api.model.ContactsRemote

interface ContactsServiceApi {
    suspend fun contacts() : ContactsRemote
    companion object{
        val RANDOM_USER_BASE_URL = "https://randomuser.me/api/"
    }

}