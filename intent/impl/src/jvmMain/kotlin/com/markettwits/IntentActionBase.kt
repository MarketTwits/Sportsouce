package com.markettwits

import java.awt.Desktop
import java.net.URI

class IntentActionBase : IntentAction {
    override fun openWebPage(url: String) {
        val desktop = Desktop.getDesktop()
        desktop.browse(URI.create(url))
    }

    override fun openPhone(phone: String) {
        throw NoSuchMethodException("don't implements in jvm target")
    }
}