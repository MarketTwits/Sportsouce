package com.markettwits.selfupdater.thirdparty.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.markettwits.selfupdater.models.ReleaseNotesLocale

class WebDeploymentTest {
    private val next = WebDeployment(1, 22, "1.6.1", 10422, mapOf("ru" to "Исправления", "en" to "Fixes"))

    @Test
    fun comparesPublishedSequenceAndDoesNotOfferOlderBuilds() {
        assertTrue(next.isNewerThan(21, 10422))
        assertFalse(next.isNewerThan(22, 10422))
        assertFalse(next.isNewerThan(21, 10423))
        assertFailsWith<IllegalArgumentException> { next.isNewerThan(0, 10422) }
        assertFailsWith<IllegalArgumentException> { next.copy(schemaVersion = 2).isNewerThan(21, 10422) }
    }

    @Test
    fun russianAppContentNeverFallsBackToEnglish() {
        assertEquals("Исправления", next.description())
        assertEquals("Исправления", next.description("ru-RU"))
        assertEquals("Исправления", next.description("RU_ru"))
        assertEquals("Regional", next.copy(changes = mapOf("en-US" to "Regional")).description("EN_us"))
        assertEquals("Fixes", next.description("fr-FR"))
        assertEquals(ReleaseNotesLocale.unavailable(), next.copy(changes = emptyMap()).description("ru"))
        assertEquals(ReleaseNotesLocale.unavailable(), next.copy(changes = mapOf("ru" to " ", "en" to "Fixes")).description())
        assertEquals("Русский", next.copy(changes = mapOf("ru-RU" to "Русский", "en" to "Fixes")).description("ru"))
    }
}
