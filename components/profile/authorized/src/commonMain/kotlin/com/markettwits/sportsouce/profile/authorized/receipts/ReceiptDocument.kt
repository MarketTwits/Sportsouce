package com.markettwits.sportsouce.profile.authorized.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceColors
import com.markettwits.core_ui.items.theme.SauceTheme

/** A read-only, self-contained server document. No credentials are passed to the viewer. */
@Composable
internal expect fun ReceiptDocument(html: String, modifier: Modifier = Modifier)

internal data class ReceiptDocumentColors(val background: Color, val text: Color, val border: Color, val dark: Boolean)

internal fun receiptDocumentColors(colors: SauceColors, dark: Boolean) = ReceiptDocumentColors(
    background = if (dark) colors.background else colors.surface,
    text = colors.textPrimary,
    border = colors.border,
    dark = dark,
)

@Composable
internal fun rememberReceiptDocumentColors(): ReceiptDocumentColors {
    val colors = SauceTheme.colors
    val dark = LocalDarkOrLightTheme.current
    return remember(colors, dark) { receiptDocumentColors(colors, dark) }
}

private fun Color.cssHex() = "#" + (toArgb() and 0xFFFFFF).toString(16).padStart(6, '0')

internal fun receiptDisplayHtml(html: String, colors: ReceiptDocumentColors): String {
    val head = """
        <meta http-equiv="Content-Security-Policy" content="default-src 'none'; style-src 'unsafe-inline'; img-src data:; base-uri 'none'; form-action 'none'">
    """.trimIndent()
    val style = """
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
        html,body{margin:0!important;padding:0!important;background:${colors.background.cssHex()}!important;color:${colors.text.cssHex()}!important;color-scheme:${if (colors.dark) "dark" else "light"}}
        body *{color:inherit!important;background-color:transparent!important}
        .line,hr,table,th,td{border-color:${colors.border.cssHex()}!important}
        body{font-size:14px;line-height:1.5;overflow-wrap:break-word}
        .receipt{box-sizing:border-box!important;width:100%!important;max-width:668px!important;margin:0 auto!important;padding:24px 16px!important}
        table{width:100%!important;table-layout:fixed}th,td{overflow-wrap:anywhere;padding:6px 3px!important;font-size:12px}
        th:first-child,td:first-child{width:22px}th:nth-last-child(2),td:nth-last-child(2){width:32px}th{white-space:nowrap}
        th:last-child,td:last-child{width:76px}.sign{gap:16px}.total{font-size:18px}
        @media print{
        html,body{background:#fff!important;color:#111!important;color-scheme:light}
        body *{color:#111!important;background-color:transparent!important}
        .line,hr,table,th,td{border-color:#333!important}
        }
        </style>
    """.trimIndent()
    // Append sizing after the server stylesheet so a fixed desktop width cannot win.
    val closingHead = Regex("</head\\s*>", RegexOption.IGNORE_CASE)
    return if (closingHead.containsMatchIn(html)) {
        val withPolicy = Regex("<head\\b[^>]*>", RegexOption.IGNORE_CASE).replaceFirst(html, "$0$head")
        closingHead.replaceFirst(withPolicy, "$style</head>")
    } else {
        "<!doctype html><html><head><meta charset=\"utf-8\">$head$style</head><body>$html</body></html>"
    }
}

/** The receipt endpoint returns major currency units, unlike registration prices in kopecks. */
internal fun receiptAmount(amount: String?): String {
    val match = Regex("^(-?)([0-9]+)(?:\\.([0-9]{1,2}))?$").matchEntire(amount?.trim().orEmpty()) ?: return "—"
    val whole = match.groupValues[2].trimStart('0').ifEmpty { "0" }
    val grouped = whole.reversed().chunked(3).joinToString("\u00a0").reversed()
    val fraction = match.groupValues[3].padEnd(2, '0').takeIf { it != "00" }?.let { ",$it" }.orEmpty()
    return "${match.groupValues[1]}$grouped$fraction ₽"
}
