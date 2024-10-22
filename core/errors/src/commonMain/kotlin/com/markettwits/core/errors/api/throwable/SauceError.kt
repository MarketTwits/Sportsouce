package com.markettwits.core.errors.api.throwable

sealed class SauceError(private val throwable: Throwable) : Throwable(throwable){

    data class Connection(val exception : Throwable) : SauceError(exception)

    data class WrongRequest(val exception : Throwable) : SauceError(exception)

    data class JsonConverter(val exception: Throwable) : SauceError(exception)

    data class NotFound(val exception: Throwable) : SauceError(exception)

    data class Empty(val exception: Throwable) : SauceError(exception)

    data class General(val exception: Throwable) : SauceError(exception)
}