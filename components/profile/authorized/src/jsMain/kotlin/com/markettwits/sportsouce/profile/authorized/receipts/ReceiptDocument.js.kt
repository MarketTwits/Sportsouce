package com.markettwits.sportsouce.profile.authorized.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLIFrameElement

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun ReceiptDocument(html: String, modifier: Modifier) {
    val colors = rememberReceiptDocumentColors()
    val content = remember(html, colors) { receiptDisplayHtml(html, colors) }
    val background = "#" + (colors.background.toArgb() and 0xFFFFFF).toString(16).padStart(6, '0')
    WebElementView(
        modifier = modifier,
        factory = {
            (document.createElement("iframe") as HTMLIFrameElement).apply {
                title = "Квитанция"
                setAttribute("sandbox", "")
                style.border = "0"
                style.backgroundColor = background
            }
        },
        update = { frame ->
            frame.style.backgroundColor = background
            if (frame.srcdoc != content) frame.srcdoc = content
        },
    )
}
