package com.markettwits.core_ui.items.text

import androidx.compose.ui.text.AnnotatedString

internal expect fun htmlToAnnotatedStringInner(
    html: String,
    compactMode: Boolean = false,
) : AnnotatedString