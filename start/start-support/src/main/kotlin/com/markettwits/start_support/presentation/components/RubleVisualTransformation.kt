package com.markettwits.start_support.presentation.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RubleSymbolVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val newText = if (text.text.isNotEmpty()) {
            AnnotatedString.Builder(text).apply {
                append(" ₽")
            }.toAnnotatedString()
        } else {
            text
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset < text.text.length) offset else text.text.length
            }
        }
        return TransformedText(newText, mapping)
    }
}
