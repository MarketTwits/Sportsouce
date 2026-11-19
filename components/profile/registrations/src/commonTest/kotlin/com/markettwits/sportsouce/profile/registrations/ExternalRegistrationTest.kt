package com.markettwits.sportsouce.profile.registrations

import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistration
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistrationPage
import com.markettwits.sportsouce.profile.registrations.presentation.management.*
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.*

class ExternalRegistrationTest {
    private val draft = ExternalRegistrationDraft(
        id = 52, startName = " Тестовый старт ", distanceName = " 10 км ", startDate = "2.3.2024",
        url = " organizer.example/event ", ageGroup = "", team = " Клуб ", posterId = null, poster = null,
    )

    @Test
    fun reportsAllRequiredFieldsTogetherAndNamesInvalidDateAndUrl() {
        assertEquals(ExternalRegistrationField.entries.toSet(),
            draft.copy(startName = " ", distanceName = "", startDate = "", url = "").errors().keys)
        assertEquals(setOf(ExternalRegistrationField.Date, ExternalRegistrationField.Url),
            draft.copy(startDate = "29.02.2025", url = "https:///missing-host").errors().keys)
    }

    @Test
    fun normalizesDatesForTheServerAndSendsNullWhenThePosterIsRemoved() {
        assertTrue(draft.errors().isEmpty())
        val body = draft.body(null)
        assertEquals("2024-03-02", body.getValue("startDate").jsonPrimitive.contentOrNull)
        assertEquals("Тестовый старт", body.getValue("startName").jsonPrimitive.contentOrNull)
        assertEquals("organizer.example/event", body.getValue("url").jsonPrimitive.contentOrNull)
        assertEquals("", body.getValue("ageGroup").jsonPrimitive.contentOrNull)
        assertEquals(JsonNull, body["posterId"])
        assertEquals("2024-02-29", parseExternalRegistrationDate(" 2024-02-29 ").toString())
    }

    @Test
    fun acceptsExistingStartSlugsAndOrganizerLinksAndRejectsMalformedAddresses() {
        assertEquals("https://sportsauce.ru/starts/park-run", externalRegistrationUrl("/starts/park-run"))
        assertEquals("https://sportsauce.ru/starts/парк-1", externalRegistrationUrl("парк-1"))
        assertEquals("https://organizer.example/events/23?type=trail#run",
            externalRegistrationUrl("organizer.example/events/23?type=trail#run"))
        assertEquals("http://organizer.example:8080/", externalRegistrationUrl("HTTP://organizer.example:8080/"))
        for (invalid in listOf("https://", "a b", "https:///organizer.example", "https://organizer..example",
            "mailto:mail@organizer.example", "ftp://organizer.example", "//organizer.example", "https://name@organizer.example",
            "https://organizer.example:70000/", "unknown/path", "https://organizer.example/a\\b")) {
            assertNull(externalRegistrationUrl(invalid), invalid)
        }
    }

    @Test
    fun validatesPosterBytesRatherThanRelyingOnTheFilename() {
        val jpegHeader = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        assertEquals("image/jpeg", checkedRegistrationPoster("poster", jpegHeader, 0L).mimeType)
        assertFailsWith<IllegalArgumentException> { checkedRegistrationPoster("fake.png", "not an image".encodeToByteArray(), 0L) }
        assertFailsWith<IllegalArgumentException> { checkedRegistrationPoster("large.jpg", ByteArray(MaxPosterBytes + 1), 0L) }
    }

    @Test
    fun keepsTheServerOffsetWhenPagesOverlapAndRefreshesDuplicateEntries() {
        val first = ExternalRegistrationWindow().append(ExternalRegistrationPage(5, listOf(entry(1), entry(2))))
        val second = first.append(ExternalRegistrationPage(5, listOf(entry(2).copy(team = "Клуб"), entry(3))))
        assertEquals(listOf(1, 2, 3), second.rows.map { it.id })
        assertEquals("Клуб", second.rows[1].team)
        assertEquals(4, second.nextOffset)
        assertTrue(second.hasMore)
        assertFalse(second.append(ExternalRegistrationPage(5, listOf(entry(4)))).hasMore)
        assertFalse(second.append(ExternalRegistrationPage(5, emptyList())).hasMore)
        assertEquals(listOf(1, 3), second.without(2).rows.map { it.id })
    }

    private fun entry(id: Int) = ExternalRegistration(id, "Старт", "10 км", "2024-03-02", "https://organizer.example")
}
