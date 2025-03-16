package com.markettwits.core_ui.items.text

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

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
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    selectable: Boolean = false,
) {
    CommonHtmlText(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        selectable = selectable,
        softWrap = softWrap,
    )
}

@Composable
internal expect fun CommonHtmlText(
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
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    selectable: Boolean,
)

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun DefaultHtmlText(
    modifier: Modifier = Modifier,
    annotatedString: AnnotatedString,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    selectable: Boolean,
    maxLines: Int = Int.MAX_VALUE,
) {
    val handler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val pressIndicator = if (annotatedString.containsLinks()) modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val position = layoutResult.getOffsetForPosition(pos)
                annotatedString
                    .getUrlAnnotations(position, position)
                    .firstOrNull()?.let { range -> handler.openUri(range.item.url) }
            }
        }
    } else modifier

    val selectionColor = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    )
    CompositionLocalProvider(LocalTextSelectionColors provides selectionColor) {
        if (selectable) {
            SelectionContainer(content = {
                Text(
                    text = annotatedString,
                    modifier = modifier.then(pressIndicator),
                    style = TextStyle(
                        color = color,
                        fontSize = fontSize,
                        fontStyle = fontStyle,
                        fontWeight = fontWeight,
                        fontFamily = fontFamily,
                        letterSpacing = letterSpacing,
                        textDecoration = textDecoration,
                        textAlign = textAlign ?: TextAlign.Start,
                        lineHeight = lineHeight,
                    ),
                    softWrap = softWrap,
                    overflow = overflow,
                    maxLines = maxLines,
                    onTextLayout = {
                        layoutResult.value = it
                    },
                )
            })
        } else {
            Text(
                text = annotatedString,
                modifier = modifier.then(pressIndicator),
                style = TextStyle(
                    color = color,
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    letterSpacing = letterSpacing,
                    textDecoration = textDecoration,
                    textAlign = textAlign ?: TextAlign.Start,
                    lineHeight = lineHeight,
                ),
                softWrap = softWrap,
                overflow = overflow,
                maxLines = maxLines,
                onTextLayout = {
                    layoutResult.value = it
                }
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
internal fun AnnotatedString.containsLinks(): Boolean = getUrlAnnotations(0, length).isNotEmpty()


