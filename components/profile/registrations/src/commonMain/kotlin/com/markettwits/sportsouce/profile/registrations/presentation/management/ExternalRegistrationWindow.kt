package com.markettwits.sportsouce.profile.registrations.presentation.management

import com.markettwits.sportsouce.profile.cloud.SportSauceRegistrationManagementApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistration
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistrationPage

internal data class ExternalRegistrationWindow(
    val rows: List<ExternalRegistration> = emptyList(),
    val nextOffset: Int = 0,
    val hasMore: Boolean = false,
) {
    fun append(page: ExternalRegistrationPage): ExternalRegistrationWindow {
        val offset = nextOffset + page.rows.size
        val merged = rows.associateBy { it.id }.toMutableMap()
        page.rows.forEach { merged[it.id] = it }
        val more = page.rows.isNotEmpty() &&
            (page.count?.let { offset < it } ?: (page.rows.size >= SportSauceRegistrationManagementApi.ExternalPageSize))
        return ExternalRegistrationWindow(merged.values.toList(), offset, more)
    }

    fun without(id: Int): ExternalRegistrationWindow {
        val updated = rows.filterNot { it.id == id }
        if (updated.size == rows.size) return this
        return copy(rows = updated, nextOffset = (nextOffset - 1).coerceAtLeast(0))
    }
}
