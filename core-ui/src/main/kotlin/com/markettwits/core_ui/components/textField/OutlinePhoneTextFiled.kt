package com.markettwits.core_ui.components.textField

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.markettwits.core_ui.components.textField.transformations.NumberDefaults
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    onValueChange: (String) -> Unit
) {
    OutlinedTextFieldBase(
        modifier = modifier,
        label = label,
        value = value,
        isError = isError,
        isEnabled = isEnabled,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onValueChange = { it ->
            if (it.length <= NumberDefaults.INPUT_LENGTH) { // Максимальная длина для +7 (XXX) XXX-XX-XX
                onValueChange(it)
            }
        }
    )
}
class MaskVisualTransformation(private val mask: String) : VisualTransformation {
    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        val cleanedText = cleanPhoneNumber(text.text)
        val originalOffset = text.text.length
        var transformedOffset = 0

        text.forEachIndexed { index, char ->
            if (maskIndex < mask.length) {
                if (mask[maskIndex] == '#') {
                    if (transformedOffset < cleanedText.length) {
                        out += cleanedText[transformedOffset++]
                    }
                } else {
                    out += mask[maskIndex]
                }
                maskIndex++
            }
            if (index == originalOffset) {
                transformedOffset = out.length
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

//    override fun filter(text: AnnotatedString): TransformedText {
//        var out = ""
//        var maskIndex = 0
//        val cleanedText = removeSpecialCharacters(text.text)
//        cleanedText.forEach { char ->
//            while (specialSymbolsIndices.contains(maskIndex)) {
//                out += mask[maskIndex]
//                maskIndex++
//            }
//            out += char
//            maskIndex++
//        }
//        return TransformedText(AnnotatedString(out), offsetTranslator())
//    }

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

fun cleanPhoneNumber(phoneNumber: String): String {
    // Remove spaces, brackets, and prefix "+7"
    return phoneNumber.replace("^\\+7".toRegex(), "").replace("[\\s()\\[\\]-]".toRegex(), "")
}

fun removeSpecialCharacters(input: String): String {
    return input.replace("^\\+7".toRegex(), "").replace("[^0-9]".toRegex(), "")
}

//class MaskVisualTransformation(private val mask: String) : VisualTransformation {
//    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }
//
//    override fun filter(text: AnnotatedString): TransformedText {
//        var out = ""
//        var maskIndex = 0
//        text.forEach { char ->
//            while (specialSymbolsIndices.contains(maskIndex)) {
//                out += mask[maskIndex]
//                maskIndex++
//            }
//            out += char
//            maskIndex++
//        }
//        return TransformedText(AnnotatedString(out), offsetTranslator())
//    }
//
//    private fun offsetTranslator() = object : OffsetMapping {
//        override fun originalToTransformed(offset: Int): Int {
//            val offsetValue = offset.absoluteValue
//            if (offsetValue == 0) return 0
//            var numberOfHashtags = 0
//            val masked = mask.takeWhile {
//                if (it == '#') numberOfHashtags++
//                numberOfHashtags < offsetValue
//            }
//            return masked.length + 1
//        }
//
//        override fun transformedToOriginal(offset: Int): Int {
//            return mask.take(offset.absoluteValue).count { it == '#' }
//        }
//    }
//}