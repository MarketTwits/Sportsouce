package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue


@Composable
fun OutlinePhoneTextFiled(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = MaskVisualTransformation(NumberDefaults.MASK),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done
    ),
    onValueChange: (String) -> Unit
) {
    var texts = mapFullRuPhoneNumberToSimple(value)

    OutlinedTextFieldBase(
        modifier = modifier,
        label = label,
        value = texts,
        isError = isError,
        isEnabled = isEnabled,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onValueChange = {
            val filteredValue = it.filterIndexed { index, char ->
                char.isDigit() || (index == 0 && char == '+')
            }
            if (filteredValue.length <= NumberDefaults.INPUT_LENGTH) {
                texts = filteredValue
                onValueChange(visualTransformation.filter(AnnotatedString(filteredValue)).text.text)
            }
        }
    )
}

class MaskVisualTransformation(private val mask: String) : VisualTransformation {
    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}

fun mapFullRuPhoneNumberToSimple(input: String): String {
    var cleanedNumber = input
        .replace("(", "")
        .replace(")", "")
        .replace("-", "")
        .replace("+7", "")
        .replace("\\s+".toRegex(), "")
    if (cleanedNumber.startsWith("7")) {
        cleanedNumber = cleanedNumber.substring(1)
    }
    return cleanedNumber
}

fun mapSimpleRuPhoneNumberToFull(input: String) : String{
    val formattedNumber = input
        .replaceFirst("^(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})$"
            .toRegex(), "+$1 ($2) $3-$4-$5")
    return formattedNumber
}

object NumberDefaults {
    const val MASK = "+7 (###) ###-##-##"
    const val INPUT_LENGTH = 10
}
