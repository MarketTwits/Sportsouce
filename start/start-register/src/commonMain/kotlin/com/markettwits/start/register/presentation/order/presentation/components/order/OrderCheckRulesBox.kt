package com.markettwits.start.register.presentation.order.presentation.components.order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun OrderCheckRulesBox(
    isChecked: Boolean,
    onClickRulesCheck: () -> Unit,
    onClickPrivacyPolicy: (String) -> Unit
) {
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
        PrivacyPolicyText(onClickPrivacyPolicy)
    }
}

@Composable
private fun PrivacyPolicyText(onClickPrivacyPolicy: (String) -> Unit) {
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
                onClickPrivacyPolicy("https://sportsauce.ru/confidentiality")
            }
        }
    )
}