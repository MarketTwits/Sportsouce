package com.markettwits.core_ui.items.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun ClickableText(
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
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current,
    onPhoneClick: ((String) -> Unit)? = null,
    onLinkClick: ((String) -> Unit)? = null
) {

    val localUriHandler = LocalUriHandler.current
    val linkColor = MaterialTheme.colorScheme.secondary
    val annotatedText = remember(text, linkColor, onPhoneClick, onLinkClick, localUriHandler) {
        buildAnnotatedString {
            append(text)
            val linkStyle = TextLinkStyles(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
            val urls = urlRegex.findAll(text).toList()
            urls.forEach { match ->
                val url = match.value.trimEnd('.', ',', ';', ':')
                addLink(
                    LinkAnnotation.Url(url, linkStyle) {
                        if (onLinkClick != null) onLinkClick(url) else localUriHandler.openUri(url)
                    },
                    start = match.range.first,
                    end = match.range.first + url.length,
                )
            }
            // Phone actions are opt-in. Ordinary numbers in user text keep their original appearance.
            if (onPhoneClick != null) phoneRegex.findAll(text).forEach { match ->
                val digits = match.value.count(Char::isDigit)
                val overlapsUrl = urls.any { it.range.first <= match.range.last && match.range.first <= it.range.last }
                if (digits in 7..15 && !overlapsUrl) {
                    addLink(
                        LinkAnnotation.Clickable("phone", linkStyle) { onPhoneClick(match.value) },
                        start = match.range.first,
                        end = match.range.last + 1,
                    )
                }
            }
        }
    }

    val textStyle = style.merge(
        TextStyle(
            color = if (color != Color.Unspecified) color else style.color,
            fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
            fontStyle = fontStyle ?: style.fontStyle,
            fontWeight = fontWeight ?: style.fontWeight,
            fontFamily = fontFamily ?: style.fontFamily,
            letterSpacing = if (letterSpacing != TextUnit.Unspecified) letterSpacing else style.letterSpacing,
            textDecoration = textDecoration ?: style.textDecoration,
            textAlign = textAlign ?: style.textAlign,
            lineHeight = if (lineHeight != TextUnit.Unspecified) lineHeight else style.lineHeight
        )
    )

    Text(
        text = annotatedText,
        modifier = modifier,
        style = textStyle,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
    )
}

private val phoneRegex = Regex("""(?<![\p{L}\d])\+?\d[\d ()-]*\d(?![\p{L}\d])""")
private val urlRegex = Regex("""https?://[^\s<>]+""")
