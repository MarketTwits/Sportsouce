package com.markettwits.core_ui.items.text

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText

/**
 * Simple Text composable to show the text with html styling from string
 * resources. Supported are:
 *
 * **Bold**
 *
 * *Italic*
 *
 * <u>Underlined</u>
 *
 * <strike>Strikethrough</strike>
 *
 * <a href="https://google.de">Link</a>
 *
 * @see androidx.compose.material.Text
 */
@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    selectable: Boolean = false,
    @Suppress("warning can be crash,")
    softWrap: Boolean = true,
    @Suppress("warning can be crash,")
    maxLines: Int = Int.MAX_VALUE,
    @Suppress("warning can be crash,")
    overflow: TextOverflow = TextOverflow.Visible,
) {
    val state = rememberRichTextState()

    LaunchedEffect(text) {
        state.setHtml(text)
        state.config.linkColor = color
        state.config.codeSpanColor = color
        state.config.codeSpanBackgroundColor = color
        state.config.codeSpanStrokeColor = color
    }

    val selectionColor = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    )

    val richText = @Composable {
        RichText(
            state = state,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign ?: TextAlign.Unspecified,
        )
    }

    if (selectable) {
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColor) {
            SelectionContainer(content = { richText() })
        }
    } else {
        richText()
    }
}