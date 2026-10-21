package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*


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
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = MaskVisualTransformation(NumberDefaults.MASK),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
) {
    val texts = mapFullRuPhoneNumberToSimple(value)

    OutlinedTextFieldBase(
        modifier = modifier,
        label = label,
        value = texts,
        isError = isError,
        errorMessage = errorMessage,
        isEnabled = isEnabled,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        supportingText = supportingText,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onValueChange = {
            val normalizedValue = normalizeRuPhoneInput(it)
            onValueChange(visualTransformation.filter(AnnotatedString(normalizedValue)).text.text)
        }
    )
}

class MaskVisualTransformation(private val mask: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalOffsets = IntArray(text.length + 1)
        val transformedOffsets = mutableListOf(0)
        val formatted = StringBuilder()
        var maskIndex = 0
        text.forEachIndexed { index, char ->
            while (maskIndex < mask.length && mask[maskIndex] != '#') {
                formatted.append(mask[maskIndex++])
                transformedOffsets.add(index)
            }
            formatted.append(char)
            transformedOffsets.add(index + 1)
            originalOffsets[index + 1] = formatted.length
            maskIndex++
        }
        // Build both directions from the actual output, including partial and overflowing input.
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                originalOffsets[offset.coerceIn(0, text.length)]

            override fun transformedToOriginal(offset: Int): Int =
                transformedOffsets[offset.coerceIn(0, formatted.length)]
        }
        return TransformedText(AnnotatedString(formatted.toString()), mapping)
    }
}

fun mapFullRuPhoneNumberToSimple(input: String): String {
    return normalizeRuPhoneInput(input)
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

private fun normalizeRuPhoneInput(input: String): String {
    val trimmedInput = input.trim()
    val digitsOnly = input.filter(Char::isDigit)

    if (digitsOnly.isEmpty()) return ""

    val normalizedDigits = when {
        trimmedInput.startsWith("+7") -> digitsOnly.drop(1).take(NumberDefaults.INPUT_LENGTH)
        digitsOnly.length == NumberDefaults.INPUT_LENGTH + 1 &&
                (digitsOnly.first() == '7' || digitsOnly.first() == '8') -> {
            digitsOnly.drop(1).take(NumberDefaults.INPUT_LENGTH)
        }

        digitsOnly.length <= NumberDefaults.INPUT_LENGTH -> digitsOnly
        digitsOnly.first() == '7' || digitsOnly.first() == '8' -> {
            digitsOnly.drop(1).take(NumberDefaults.INPUT_LENGTH)
        }

        else -> digitsOnly.take(NumberDefaults.INPUT_LENGTH)
    }

    return normalizedDigits.take(NumberDefaults.INPUT_LENGTH)
}
