package com.markettwits.sportsouce.profile.authorized.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.toArgb
import javax.swing.JEditorPane
import javax.swing.JScrollPane
import javax.swing.text.html.HTMLEditorKit

@Composable
internal actual fun ReceiptDocument(html: String, modifier: Modifier) {
    val colors = rememberReceiptDocumentColors()
    val document = remember(html, colors) { receiptDisplayHtml(html, colors) }
    val background = java.awt.Color(colors.background.toArgb(), true)
    SwingPanel(
        background = colors.background,
        modifier = modifier,
        factory = { JScrollPane(JEditorPane().apply {
            isEditable = false
            editorKit = HTMLEditorKit()
            putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true)
        }).apply { border = null; viewport.background = background } },
        update = { scroll ->
            val editor = scroll.viewport.view as JEditorPane
            scroll.viewport.background = background
            editor.background = background
            editor.foreground = java.awt.Color(colors.text.toArgb(), true)
            if (editor.getClientProperty("receipt") != document) {
                editor.putClientProperty("receipt", document)
                editor.text = document
                editor.caretPosition = 0
            }
        },
    )
}
