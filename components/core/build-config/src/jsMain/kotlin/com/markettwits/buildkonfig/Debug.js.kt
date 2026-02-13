package com.markettwits.buildkonfig

actual val isDebugMode: Boolean
    get() = resolveDebugMode()

private fun resolveDebugMode(): Boolean {
    val explicit = js(
        "typeof globalThis !== 'undefined' && typeof globalThis.__SPORTSAUCE_DEBUG__ === 'boolean'" +
                " ? globalThis.__SPORTSAUCE_DEBUG__ : null"
    )
    if (explicit is Boolean) {
        return explicit
    }
    val nodeEnv = js(
        "typeof process !== 'undefined' && process.env && process.env.NODE_ENV" +
                " ? process.env.NODE_ENV : null"
    )
    if (nodeEnv is String) {
        return nodeEnv != "production"
    }
    return false
}
