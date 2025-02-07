package com.markettwits

class EmptyIntentAction : IntentAction {

    override fun openWebPage(url: String) = Unit

    override fun openPhone(phone: String) = Unit

    override fun copyToSystemBuffer(text: String) = Unit

    override fun sharePlainText(text: String) = Unit
}