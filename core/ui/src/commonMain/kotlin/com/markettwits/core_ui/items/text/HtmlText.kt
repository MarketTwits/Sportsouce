package com.markettwits.core_ui.items.text

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import be.digitalia.compose.htmlconverter.htmlToString
import kotlinx.coroutines.launch

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
    maxLines: Int = Int.MAX_VALUE,
) {
    val annotatedString = htmlToAnnotatedString(
        html = text,
    )
    println(annotatedString)

    HtmlText(
        modifier = modifier,
        annotatedString = annotatedString,
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
        softWrap = softWrap,
        maxLines = maxLines,
    )
}

/**
 * Simple Text composable to show the text with html styling from a String.
 * Supported are:
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
@OptIn(ExperimentalTextApi::class)
@Composable
fun HtmlText(
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

    BasicText(
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

@OptIn(ExperimentalTextApi::class)
internal fun AnnotatedString.containsLinks(): Boolean = getUrlAnnotations(0, length).isNotEmpty()


