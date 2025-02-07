package com.markettwits.core_ui.items.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

internal actual fun htmlToAnnotatedStringInner(
    html: String,
    compactMode: Boolean
): AnnotatedString {
    return AnnotatedString.Builder().toAnnotatedString()
    val parser = js("new DOMParser()")
    val doc = parser.parseFromString(html, "text/html")
    val body = doc.body

    val builder = AnnotatedString.Builder()

    fun traverse(node: dynamic) {
        when (node.nodeType) {
            3 -> { // Text Node
                builder.append(node.nodeValue as String)
            }
            1 -> { // Element Node
                val tag = node.tagName.toLowerCase()
                when (tag) {
                    "b", "strong" -> builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    "i", "em" -> builder.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    "u" -> builder.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                }
                val childNodes = node.childNodes
                for (i in 0 until childNodes.length) {
                    traverse(childNodes[i])
                }
                when (tag) {
                    "b", "strong", "i", "em", "u" -> builder.pop()
                }
            }
        }
    }

    if (body != null) {
        val childNodes = body.childNodes
        for (i in 0 until childNodes.length as Int) {
            traverse(childNodes[i])
        }
    }
    return builder.toAnnotatedString()
}