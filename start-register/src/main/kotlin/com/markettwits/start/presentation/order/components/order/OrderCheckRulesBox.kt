package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.markettwits.core_ui.base_extensions.openWebPage

@Composable
fun OrderCheckRulesBox(isChecked: Boolean, onClickRulesCheck: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onCheckedChange = {
                onClickRulesCheck()
            })
        PrivacyPolicyText()
    }
}

@Composable
private fun PrivacyPolicyText() {
    val context = LocalContext.current
    val consentText = buildAnnotatedString {
        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.LightGray)) {
            append("Я согласен с условиями проведения старта и ")
        }
        pushStringAnnotation("PrivacyPolicy", "Политикой конфиденциальности")
        withStyle(style = androidx.compose.ui.text.SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            append("Политикой конфиденциальности")
        }
        pop()
    }
    ClickableText(
        text = consentText,
        onClick = { offset ->
            val annotations = consentText.getStringAnnotations("PrivacyPolicy", offset, offset)
            if (annotations.isNotEmpty()) {
                val url = "https://sportsauce.ru/confidentiality"
                openWebPage(url, context)
            }
        }
    )
}