package com.markettwits

interface IntentAction {
    fun openWebPage(url: String)
    fun openPhone(phone: String)
    fun copyToSystemBuffer(text: String)
}