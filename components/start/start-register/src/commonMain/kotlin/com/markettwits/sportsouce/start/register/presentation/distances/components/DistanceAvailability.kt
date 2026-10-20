package com.markettwits.sportsouce.start.register.presentation.distances.components

import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance

internal data class DistanceAvailability(
    val label: String,
    val tone: SauceStatusTone,
    val canSelect: Boolean,
)

internal fun DistinctDistance.availability(): DistanceAvailability {
    if (infiniteSlots) return DistanceAvailability("Места есть", SauceStatusTone.Success, true)

    // Keep the existing mobile fallback for responses without open_slots.
    val remaining = openSlots?.coerceAtLeast(0)
        ?: slots?.let { (it - (takenSlots ?: 0)).coerceAtLeast(0) }
        ?: 0
    return when {
        remaining == 0 -> DistanceAvailability("Мест нет", SauceStatusTone.Danger, false)
        remaining <= 10 -> DistanceAvailability("Осталось мест: $remaining", SauceStatusTone.Warning, true)
        else -> DistanceAvailability("Осталось мест: $remaining", SauceStatusTone.Success, true)
    }
}
