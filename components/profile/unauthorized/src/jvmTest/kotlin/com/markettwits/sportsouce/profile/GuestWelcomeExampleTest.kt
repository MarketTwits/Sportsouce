package com.markettwits.sportsouce.profile

import com.markettwits.sportsouce.unauthorized.data.parseGuestWelcomeConfig
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GuestWelcomeExampleTest {
    @Test fun liveConfigIsAcceptedByTheSameParserAsTheApp() {
        val content = parseGuestWelcomeConfig(File("../../../.github/content/guest-profile.json").readText()).contents()
        assertTrue(content.isNotEmpty())
        assertEquals(content.size, content.map { it.id }.distinct().size)
    }

    @Test fun publishedExampleIsAcceptedByTheSameParserAsTheApp() {
        val content = parseGuestWelcomeConfig(File("remote-content/guest-profile.example.json").readText()).contents()
        assertEquals(listOf("trail-v1", "winter-v1"), content.map { it.id })
    }
}
