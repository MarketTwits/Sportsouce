package com.markettwits.sportsouce.start.domain

import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

internal fun Throwable.requiresStartSignIn(): Boolean =
    this is AuthException || (this is ClientRequestException && response.status == HttpStatusCode.Unauthorized)
