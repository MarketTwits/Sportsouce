package com.markettwits

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLTextAreaElement

class IntentActionWeb : IntentAction {
    override fun openWebPage(url: String) {
        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "http://$url"
        } else url
        window.open(formattedUrl, "_blank")
    }

    override fun openPhone(phone: String) {
        window.open("tel:${phone.filter { it.isDigit() }}", "_self")
    }

    override fun copyToSystemBuffer(text: String) {
        val textarea = document.createElement("textarea") as HTMLTextAreaElement
        textarea.value = text
        document.body?.appendChild(textarea)
        textarea.select()
        document.execCommand("copy")
        document.body?.removeChild(textarea)
    }

    override fun sharePlainText(text: String) {
        val navigator = window.navigator.asDynamic()
        if (navigator.share != undefined) {
            navigator.share(js("{ text: text }"))
        } else {
            openWebPage("https://t.me/share/url?text=$text") // Фолбэк на Telegram Web
        }
    }
}