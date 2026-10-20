package com.markettwits.core_ui.items.components.textField

import androidx.compose.ui.text.AnnotatedString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MaskVisualTransformationTest {
    @Test
    fun partialPhoneNumbersKeepAllCursorOffsetsValid() {
        val digits = "9134567890"
        for (length in 0..digits.length) assertOffsets(NumberDefaults.MASK, digits.take(length))
        assertEquals("+7 (913) 456-78-90", MaskVisualTransformation(NumberDefaults.MASK).filter(AnnotatedString(digits)).text.text)
    }

    @Test
    fun inputBeyondShortOrEmptyMasksDoesNotLoseCursorPositions() {
        for (mask in listOf("", "#", "(##)", "prefix", NumberDefaults.MASK)) {
            for (length in 0..14) assertOffsets(mask, "12345678901234".take(length))
        }
    }

    @Test
    fun fullAndFormattedRussianNumbersKeepTheirExistingNormalization() {
        for (input in listOf("9134567890", "+79134567890", "+7 (913) 456-78-90", "89134567890")) {
            assertEquals("9134567890", mapFullRuPhoneNumberToSimple(input))
        }
        assertEquals("913", mapFullRuPhoneNumberToSimple("913"))
        assertEquals("", mapFullRuPhoneNumberToSimple("+7"))
        assertEquals("", mapFullRuPhoneNumberToSimple(""))
    }

    private fun assertOffsets(mask: String, input: String) {
        val transformed = MaskVisualTransformation(mask).filter(AnnotatedString(input))
        val mapping = transformed.offsetMapping
        val positions = (0..input.length).map { mapping.originalToTransformed(it) }
        assertEquals(positions.sorted(), positions, "Cursor must move forwards: mask=$mask, input=$input")
        for (offset in 0..input.length) {
            val visual = mapping.originalToTransformed(offset)
            assertTrue(visual in 0..transformed.text.length, "Invalid visual position $visual for mask=$mask, input=$input")
            assertEquals(offset, mapping.transformedToOriginal(visual), "Cursor lost at $offset: mask=$mask, input=$input")
        }
        for (offset in 0..transformed.text.length) {
            assertTrue(mapping.transformedToOriginal(offset) in 0..input.length)
        }
    }
}
