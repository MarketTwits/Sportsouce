package com.markettwits.sportsouce.start.cloud.model.filters

import kotlinx.serialization.Serializable

@Serializable
data class StartOrganizersRemote(val count: Int, val rows: List<Row>) {
    @Serializable
    data class Row(val name: String?)
}
