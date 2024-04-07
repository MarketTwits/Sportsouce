package com.markettwits.inappnotification.api

data class ExceptionEvent(
    val exception: Throwable,
    val params: Map<Any, Any> = emptyMap(),
    val name: String? = null,
    val issueKey: String? = null
)
