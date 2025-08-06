package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun TermsAndPrivacyContent(
    modifier: Modifier = Modifier,
    value: String = "Войти",
) {
    val defaultPrivacyUrl = "https://sportsauce.ru/confidentiality"
    val localUriHandler = androidx.compose.ui.platform.LocalUriHandler.current


    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline
            )
        ) {
            append("Нажимая \"$value\", вы соглашаетесь с ")
        }

        pushStringAnnotation(tag = "terms", annotation = "terms")
        withStyle(
            style = SpanStyle(
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Условиями обслуживания")
        }
        pop()

        withStyle(
            style = SpanStyle(
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline
            )
        ) {
            append(" и ")
        }

        pushStringAnnotation(tag = "privacy", annotation = "privacy")
        withStyle(
            style = SpanStyle(
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Политикой конфиденциальности")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier.fillMaxWidth(),
        style = androidx.compose.ui.text.TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        ),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "terms", start = offset, end = offset)
                .firstOrNull()?.let {
                    runCatching { localUriHandler.openUri(defaultPrivacyUrl) }
                }

            annotatedText.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                .firstOrNull()?.let {
                    runCatching { localUriHandler.openUri(defaultPrivacyUrl) }
                }
        }
    )
}