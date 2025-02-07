package com.markettwits.core_ui.items.text

import androidx.compose.ui.text.AnnotatedString
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString


internal actual fun htmlToAnnotatedStringInner(
    html: String,
    compactMode: Boolean
): AnnotatedString = htmlToAnnotatedString(html, compactMode)
