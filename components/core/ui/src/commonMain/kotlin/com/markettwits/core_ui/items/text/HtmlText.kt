package com.markettwits.core_ui.items.text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import androidx.compose.material3.Text
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SauceTheme

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
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible,
    showImages: Boolean = maxLines == Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    val normalizedText = remember(text) { normalizeHtml(text) }
    val cleanedText = remember(normalizedText) {
        removeColorStylesFromHtml(removeImgTagsFromHtml(normalizedText))
    }
    val images = remember(normalizedText) { extractImageUrlsFromHtml(normalizedText) }

    val actualColor = if (color == Color.Unspecified) SauceTheme.colors.textPrimary else color
    val linkColor = SauceTheme.colors.action
    val document = rememberHtmlDocument(cleanedText, linkColor, style.merge(TextStyle(
        fontSize = fontSize, fontStyle = fontStyle, fontWeight = fontWeight, fontFamily = fontFamily,
        letterSpacing = letterSpacing, lineHeight = lineHeight,
    )))

    val selectionColor = TextSelectionColors(
        handleColor = SauceTheme.colors.action,
        backgroundColor = SauceTheme.colors.actionSoft
    )

    val richText = @Composable {
        Column(modifier) {
            // The editor's layout callback measures list markers in paragraphs that may have
            // been clipped by our parent's height. Native Text can safely display the parsed
            // document at any size without invoking that editing-only callback.
            Text(
                text = document,
                modifier = Modifier.fillMaxWidth(),
                color = actualColor,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                lineHeight = lineHeight,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign ?: TextAlign.Unspecified,
                maxLines = maxLines,
                softWrap = softWrap,
                overflow = overflow,
                style = style,
                onTextLayout = onTextLayout,
            )
            if (showImages) HtmlTextImages(
                images = images,
                onClickImage = { selectedImageUrl = it },
            )
        }
    }

    if (selectable) {
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColor) {
            SelectionContainer(content = { richText() })
        }
    } else {
        richText()
    }

    selectedImageUrl?.let { imageUrl ->
        FullImageScreen(
            image = images,
            selectedImageUrl = imageUrl,
            onDismiss = { selectedImageUrl = null }
        )
    }
}

@Composable
private fun ColumnScope.HtmlTextImages(
    images: List<String>,
    onClickImage: (String) -> Unit,
) {
    if (images.isEmpty()) return

    Spacer(modifier = Modifier.height(12.dp))

    images.forEachIndexed { index, imageUrl ->
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Изображение ${index + 1} из ${images.size}",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium)
                .clickable(role = Role.Button, onClickLabel = "Открыть изображение") { onClickImage(imageUrl) },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .shimmer(
                            tiltAngle = 30,
                            gradientColors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                                Color.Transparent,
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary)
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            },
            error = {
                Box(Modifier.fillMaxWidth().heightIn(min = 96.dp).background(SauceTheme.colors.surfaceSubtle)
                    .padding(16.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Изображение недоступно", color = SauceTheme.colors.textMuted,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        )

        if (index != images.lastIndex) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

internal fun normalizeHtml(html: String): String {
    val trimmed = html.trim()
    val unquoted = if (trimmed.startsWith("\"<") && trimmed.endsWith(">\"")) {
        trimmed.substring(1, trimmed.length - 1)
    } else {
        trimmed
    }
    return unquoted
        .replace("\\\"", "\"")
        .replace("\\/", "/")
        .replace("\uFEFF", "")
}

internal fun removeColorStylesFromHtml(html: String): String {
    val withoutColors = html.replace(
        Regex("""\bstyle\s*=\s*(["'])([\s\S]*?)\1""", RegexOption.IGNORE_CASE)
    ) { match ->
        val declarations = match.groupValues[2].split(';').filter { declaration ->
            declaration.substringBefore(':').trim().lowercase() !in setOf("color", "background", "background-color")
        }.map(String::trim).filter(String::isNotEmpty)
        if (declarations.isEmpty()) "" else "style=${match.groupValues[1]}${declarations.joinToString(";")}${match.groupValues[1]}"
    }
    return withoutColors.replace(Regex("""<font\b[^>]*>""", RegexOption.IGNORE_CASE)) { match ->
        match.value.replace(Regex("""\s+color\s*=\s*("[^"]*"|'[^']*'|[^\s>]+)""", RegexOption.IGNORE_CASE), "")
    }
}

private fun removeImgTagsFromHtml(html: String): String {
    val imageRegex = """<img\b[^>]*>""".toRegex(RegexOption.IGNORE_CASE)
    return html.replace(imageRegex, "")
}

internal fun extractImageUrlsFromHtml(html: String): List<String> {
    val regex = """<img\s+[^>]*src\s*=\s*(['"])(.*?)\1""".toRegex(RegexOption.IGNORE_CASE)
    return regex.findAll(html).map { it.groupValues[2] }.distinct().toList()
}
