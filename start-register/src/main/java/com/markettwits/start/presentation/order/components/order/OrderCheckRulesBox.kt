package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.markettwits.core_ui.components.openWebPage
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun OrderCheckRulesBox() {
    var checked by remember {
        mutableStateOf(false)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(
                checkedColor = SportSouceColor.SportSouceLighBlue,
                checkmarkColor = Color.White,
            ),
            onCheckedChange = {
                checked = !checked
            })
        PrivacyPolicyText()
    }
}

@Composable
private fun PrivacyPolicyText() {
    val context = LocalContext.current
    // AnnotatedString with a clickable span
    val consentText = buildAnnotatedString {
        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.LightGray)) {
            append("Я согласен с условиями проведения старта и ")
        }
        pushStringAnnotation("PrivacyPolicy", "Политикой конфиденциальности")
        withStyle(style = androidx.compose.ui.text.SpanStyle(color = SportSouceColor.SportSouceLighBlue)) {
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