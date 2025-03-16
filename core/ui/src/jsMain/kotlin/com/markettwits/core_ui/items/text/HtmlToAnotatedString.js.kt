package com.markettwits.core_ui.items.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList

internal actual fun htmlToAnnotatedStringInner(
    html: String,
    compactMode: Boolean
): AnnotatedString {
    val container = document.createElement("div") as HTMLElement
    container.innerHTML = html
    return parseHtmlToAnnotatedString(container)
}

private fun parseHtmlToAnnotatedString(element: Element): AnnotatedString {
    val builder = AnnotatedString.Builder()

    element.childNodes.asList().forEach { node ->
        when (node) {
            is HTMLElement -> {
                val text = node.textContent ?: ""
                builder.append(text)
            }

            else -> {
                builder.append(node.textContent ?: "")
            }
        }
    }

    return builder.toAnnotatedString()
}