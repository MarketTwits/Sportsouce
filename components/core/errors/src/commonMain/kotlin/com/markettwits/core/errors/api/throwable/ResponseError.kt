package com.markettwits.core.errors.api.throwable

import kotlinx.serialization.SerializationException

fun Throwable.isResponseException(): Boolean =
    (this.message?.contains("error", ignoreCase = true) == true ||
            this.message?.contains("response", ignoreCase = true) == true ||
            this is SerializationException)
