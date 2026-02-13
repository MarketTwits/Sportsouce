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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.Shapes
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
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    var images by remember {
        mutableStateOf(emptyList<String>())
    }

    val spaceColor = MaterialTheme.colorScheme.onBackground

    LaunchedEffect(text, color) {
        val normalizedText = normalizeHtml(text)
        val cleanedText = removeColorStylesFromHtml(removeImgTagsFromHtml(normalizedText))
        state.setHtml(cleanedText)
        val actualColor = if (color == Color.Unspecified) {
            spaceColor
        } else {
            color
        }
        state.config.linkColor = actualColor
        state.config.codeSpanColor = actualColor
        state.config.codeSpanBackgroundColor = Color.Transparent
        state.config.codeSpanStrokeColor = Color.Transparent

        images = extractImageUrlsFromHtml(normalizedText)
    }


    val selectionColor = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    )

    val richText = @Composable {
        Column {
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
            HtmlTextImages(
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
            contentDescription = "Describe image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium)
                .clickable { onClickImage(imageUrl) },
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
            }
        )

        if (index != images.lastIndex) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

private fun normalizeHtml(html: String): String {
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

private fun removeColorStylesFromHtml(html: String): String {
    return html
        .replace(Regex("""style\s*=\s*["'][^"']*color\s*:\s*[^;"']*[;"']?[^"']*["']""", RegexOption.IGNORE_CASE), "")
        .replace(Regex("""<font[^>]*color\s*=\s*["'][^"']*["'][^>]*>""", RegexOption.IGNORE_CASE)) { matchResult ->
            val fontTag = matchResult.value
            fontTag.replace(Regex("""color\s*=\s*["'][^"']*["']""", RegexOption.IGNORE_CASE), "")
        }
        .replace(
            Regex(
                """<span[^>]*style\s*=\s*["'][^"']*color\s*:\s*[^;"']*[;"']?[^"']*["'][^>]*>""",
                RegexOption.IGNORE_CASE
            )
        ) { matchResult ->
            val spanTag = matchResult.value
            val cleanedStyle = spanTag.replace(Regex("""color\s*:\s*[^;"']*[;"']?""", RegexOption.IGNORE_CASE), "")
            if (cleanedStyle.contains("""style=""")) {
                cleanedStyle.replace(Regex("""style\s*=\s*["']\s*["']""", RegexOption.IGNORE_CASE), "")
            } else {
                cleanedStyle
            }
        }
        .replace(Regex("""style\s*=\s*["']\s*["']""", RegexOption.IGNORE_CASE), "")
}

private fun removeImgTagsFromHtml(html: String): String {
    val imageRegex = """<img\b[^>]*>""".toRegex(RegexOption.IGNORE_CASE)
    return html.replace(imageRegex, "")
}

private fun extractImageUrlsFromHtml(html: String): List<String> {
    val regex = """<img\s+[^>]*src\s*=\s*(['"])(.*?)\1""".toRegex(RegexOption.IGNORE_CASE)
    return regex.findAll(html).map { it.groupValues[2] }.distinct().toList()
}
