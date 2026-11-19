package com.markettwits.sportsouce.start.presentation.series

import kotlinx.serialization.Serializable

@Serializable
enum class StartRelatedKind(val title: String) {
    Series("Серии"),
    Archive("Архив стартов"),
}
