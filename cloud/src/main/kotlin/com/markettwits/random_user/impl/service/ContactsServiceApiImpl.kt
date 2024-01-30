package com.markettwits.random_user.impl.service

import com.markettwits.random_user.api.model.ContactsRemote
import com.markettwits.random_user.api.service.ContactsServiceApi
import com.markettwits.random_user.impl.provider.HttpClientProviderImpl
import io.ktor.client.call.body
import io.ktor.client.request.get

class ContactsServiceApiImpl(private val httpClient: HttpClientProviderImpl) :
    ContactsServiceApi {
    private val json = httpClient.provideJson()
    private val client = httpClient.provideClient()
    override suspend fun contacts(): ContactsRemote {
        val response = client.get("?results=10")
        return json.decodeFromString(response.body<String>())
    }
}