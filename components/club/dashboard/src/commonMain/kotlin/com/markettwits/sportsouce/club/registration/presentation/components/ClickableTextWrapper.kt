package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun ClickableTextWrapper(
    modifier: Modifier = Modifier,
    text: String = defaultText,
    onPhoneClick: (String) -> Unit,
    onLinkClick: (String) -> Unit
) {
    val phoneRegex = """\+?\d[\d\s]+\d""".toRegex()
    val urlRegex = """https?://\S+""".toRegex()

    val annotatedText = buildAnnotatedString {
        append(text)
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

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.outline,
        ),
        overflow = TextOverflow.Ellipsis,
        onClick = { offset ->
            annotatedText.getStringAnnotations(start = offset, end = offset).firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        "PHONE" -> onPhoneClick(annotation.item)
                        "URL" -> onLinkClick(annotation.item)
                    }
                }
        }
    )
}

private val defaultText =
    "Вы можете позвонить +7 968 22 111 22 или написать нам https://t.me/sportsoyuznsk"