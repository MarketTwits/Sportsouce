package com.markettwits.sportsouce.start.register

import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.presentation.distances.components.availability
import kotlin.test.*

class DistanceAvailabilityTest {
    private val distance = DistinctDistance(null, null, null, null, format = null, id = 1,
        infiniteSlots = false, name = "Трейл", openSlots = 14, orderNumber = 1,
        slots = 100, stages = emptyList(), staticPrice = 500)

    @Test
    fun webThresholdIncludesTenPlacesAndZeroDisablesSelection() {
        for (remaining in listOf(1, 8, 10)) {
            val status = distance.copy(openSlots = remaining).availability()
            assertEquals(SauceStatusTone.Warning, status.tone)
            assertEquals("Осталось мест: $remaining", status.label)
            assertTrue(status.canSelect)
        }
        for (remaining in listOf(11, 14, 100)) {
            val status = distance.copy(openSlots = remaining).availability()
            assertEquals(SauceStatusTone.Success, status.tone)
            assertTrue(status.canSelect)
        }
        for (remaining in listOf(0, -1)) {
            val status = distance.copy(openSlots = remaining).availability()
            assertEquals(SauceStatusTone.Danger, status.tone)
            assertEquals("Мест нет", status.label)
            assertFalse(status.canSelect)
        }
    }

    @Test
    fun unlimitedDistancesRemainAvailableRegardlessOfSlotCounters() {
        for (remaining in listOf(null, 0, -1, 8)) {
            val status = distance.copy(infiniteSlots = true, openSlots = remaining, slots = 0).availability()
            assertEquals("Места есть", status.label)
            assertEquals(SauceStatusTone.Success, status.tone)
            assertTrue(status.canSelect)
        }
    }

    @Test
    fun explicitRemainingCountWinsAndMissingCountsKeepTheExistingFallback() {
        assertEquals("Осталось мест: 14", distance.copy(takenSlots = 99).availability().label)
        assertEquals("Осталось мест: 8", distance.copy(openSlots = null, takenSlots = 92).availability().label)
        assertEquals("Осталось мест: 100", distance.copy(openSlots = null).availability().label)
        assertFalse(distance.copy(openSlots = null, takenSlots = 101).availability().canSelect)
        assertFalse(distance.copy(openSlots = null, slots = null).availability().canSelect)
    }
}
