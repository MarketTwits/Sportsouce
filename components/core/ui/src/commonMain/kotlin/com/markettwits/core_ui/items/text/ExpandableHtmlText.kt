package com.markettwits.core_ui.items.text

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme

/** Collapses the rendered document, never an arbitrary substring of its HTML markup. */
@Composable
fun ExpandableHtmlText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedLines: Int = 6,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    require(collapsedLines > 0)
    if (text.isBlank()) return
    var expanded by rememberSaveable(text) { mutableStateOf(false) }
    var overflows by remember(text) { mutableStateOf(false) }
    val hasImages = remember(text) { extractImageUrlsFromHtml(normalizeHtml(text)).isNotEmpty() }
    Column(modifier.animateContentSize()) {
        HtmlText(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = SauceTheme.colors.textPrimary,
            style = style,
            selectable = expanded,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedLines,
            overflow = TextOverflow.Ellipsis,
            showImages = expanded,
            onTextLayout = { if (!expanded) overflows = it.hasVisualOverflow },
        )
        if (expanded || overflows || hasImages) {
            SauceButton(
                title = if (expanded) "Скрыть" else "Подробнее",
                onClick = { expanded = !expanded },
                variant = SauceButtonVariant.Text,
            )
        }
    }
}
