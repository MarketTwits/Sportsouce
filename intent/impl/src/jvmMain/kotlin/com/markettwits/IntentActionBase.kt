package com.markettwits

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

class IntentActionBase : IntentAction {
    override fun openWebPage(url: String) {
        val desktop = Desktop.getDesktop()
        desktop.browse(URI.create(url))
    }

    override fun openPhone(phone: String) {
        Toolkit.getDefaultToolkit()
            .systemClipboard
            .setContents(StringSelection(phone), null)
    }
}