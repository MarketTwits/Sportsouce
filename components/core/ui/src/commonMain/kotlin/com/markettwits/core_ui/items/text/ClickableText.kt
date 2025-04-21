package com.markettwits.core_ui.items.text

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
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

    val phoneRegex = """\+?\d[\d\s]+\d""".toRegex()
    val urlRegex = """https?://\S+""".toRegex()
    val localUriHandler = LocalUriHandler.current

    val annotatedText = buildAnnotatedString {
        append(text)

        // Обработка телефонных номеров
        phoneRegex.findAll(text).forEach { matchResult ->
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                ),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
            addStringAnnotation(
                tag = "PHONE",
                annotation = matchResult.value,
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Обработка URL-адресов
        urlRegex.findAll(text).forEach { matchResult ->
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                ),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
            addStringAnnotation(
                tag = "URL",
                annotation = matchResult.value,
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
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

    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedText,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    textLayoutResultState?.let { textLayoutResult ->
                        val position = textLayoutResult.getOffsetForPosition(offset)
                        annotatedText.getStringAnnotations(position, position).firstOrNull()
                            ?.let { annotation ->
                                when (annotation.tag) {
                                    "PHONE" -> onPhoneClick?.invoke(annotation.item)
                                    "URL" -> if (onLinkClick != null) {
                                        onLinkClick(annotation.item)
                                    } else {
                                        localUriHandler.openUri(annotation.item)
                                    }
                                }
                            }
                    }
                }
            },
        style = textStyle,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = {
            textLayoutResultState = it
        }
    )
}