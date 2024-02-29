package com.markettwits.core_ui.components.textField.transformations


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue

class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    private var lastText: String = ""

    override fun filter(text: AnnotatedString): TransformedText {
        val newText = text.text
        if (newText == lastText) {
            // Новый текст совпадает со старым, возвращаем неизмененный текст
            return TransformedText(text, offsetTranslator())
        }

        var out = ""
        var maskIndex = 0
        newText.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }

        lastText = newText // Обновляем последний текст

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


object NumberDefaults {
    const val MASK = "+7 (###) ###-##-##"
    const val INPUT_LENGTH = 19 // Equals to "#####-###".count { it == '#' }
}