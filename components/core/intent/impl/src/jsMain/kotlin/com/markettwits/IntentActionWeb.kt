package com.markettwits

import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

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

    override fun shareImage(byteArray: ByteArray, filename: String) {
        val uint8Array = Uint8Array(byteArray.toTypedArray())
        val blob = Blob(arrayOf(uint8Array), BlobPropertyBag(type = "image/png"))
        val url = URL.createObjectURL(blob)

        val link = (document.createElement("a") as HTMLAnchorElement).apply {
            href = url
            download = "$filename.png"
        }
        document.body?.appendChild(link)
        link.click()
        document.body?.removeChild(link)
        URL.revokeObjectURL(url)
    }

    override fun saveImage(byteArray: ByteArray, filename: String) {
        val uint8Array = Uint8Array(byteArray.toTypedArray())
        val blob = Blob(arrayOf(uint8Array), BlobPropertyBag(type = "image/png"))
        val url = URL.createObjectURL(blob)

        val link = (document.createElement("a") as HTMLAnchorElement).apply {
            href = url
            download = "$filename.png"
        }
        document.body?.appendChild(link)
        link.click()
        document.body?.removeChild(link)
        URL.revokeObjectURL(url)
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