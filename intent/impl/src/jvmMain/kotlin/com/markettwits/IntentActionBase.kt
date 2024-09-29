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
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    val uri = URI("tel:$phone")
                    desktop.browse(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                println("Browsing action is not supported on your desktop environment")
            }
        } else {
            println("Desktop is not supported on your system")
        }
    }

    override fun copyToSystemBuffer(text: String) {
        Toolkit.getDefaultToolkit()
            .systemClipboard
            .setContents(StringSelection(text), null)
    }

    override fun sharePlainText(text: String) = Unit
}