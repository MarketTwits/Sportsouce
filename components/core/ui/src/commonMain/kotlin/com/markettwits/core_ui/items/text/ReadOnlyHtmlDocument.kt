package com.markettwits.core_ui.items.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichSpanStyle
import com.mohamedrejeb.richeditor.model.RichTextState

/** Parse once, then render a display document without the editor's mutable layout bookkeeping. */
@OptIn(ExperimentalRichTextApi::class)
@Composable
internal fun rememberHtmlDocument(html: String, linkColor: Color, style: TextStyle): AnnotatedString {
    val state = remember(html) {
        RichTextState().apply {
            config.linkColor = Color.Unspecified
            config.codeSpanColor = Color.Unspecified
            config.codeSpanBackgroundColor = Color.Transparent
            config.codeSpanStrokeColor = Color.Transparent
            setHtml(html)
        }
    }
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    return remember(state, linkColor, style, density, measurer) {
        val source = state.annotatedString
        val builder = AnnotatedString.Builder(source)
        // Atomic span ranges preserve links around nested bold/italic text as native links.
        val boundaries = (listOf(0, source.length) + source.spanStyles.flatMap { listOf(it.start, it.end) })
            .distinct().sorted()
        boundaries.zipWithNext().forEach { (start, end) ->
            val link = state.getRichSpanStyle(TextRange(start, end)) as? RichSpanStyle.Link
            if (link != null) builder.addLink(
                LinkAnnotation.Url(link.url, TextLinkStyles(SpanStyle(
                    color = linkColor, textDecoration = TextDecoration.Underline,
                ))), start, end,
            )
        }
        // Measure only each list prefix, never offsets into a potentially clipped paragraph.
        source.paragraphStyles.forEach { paragraph ->
            val value = source.text.substring(paragraph.start, paragraph.end)
            val marker = listMarker.find(value)?.value ?: return@forEach
            val prefix = source.subSequence(paragraph.start, paragraph.start + marker.length)
            val width = measurer.measure(
                AnnotatedString(prefix.text, spanStyles = prefix.spanStyles),
                style = style, softWrap = false, maxLines = 1,
            ).size.width
            val firstLine = paragraph.item.textIndent?.firstLine ?: return@forEach
            builder.addStyle(paragraph.item.copy(textIndent = TextIndent(
                firstLine = firstLine,
                restLine = with(density) { (firstLine.value + width.toSp().value).sp },
            )), paragraph.start, paragraph.end)
        }
        builder.toAnnotatedString()
    }
}

private val listMarker = Regex("^(?:[•◦▪●○■]|[0-9]+[.)])\\s+")
