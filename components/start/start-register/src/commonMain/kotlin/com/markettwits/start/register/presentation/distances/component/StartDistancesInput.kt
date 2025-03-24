package com.markettwits.start.register.presentation.distances.component

import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import kotlinx.serialization.Serializable

@Serializable
data class StartDistancesInput(
    val startId: Int,
    val startTitle : String,
    val distance: List<DistinctDistance>,
    val mapDistance: List<Distance>,
    val paymentDisabled: Boolean,
    val paymentType: String,
)