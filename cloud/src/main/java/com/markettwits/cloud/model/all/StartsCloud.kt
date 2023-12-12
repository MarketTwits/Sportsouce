package ru.alexpanov.core_network.model.all

import com.markettwits.sportsourcedemo.all.Row
import kotlinx.serialization.Serializable

@Serializable
data class StartsCloud(
    val count: Int,
    val rows: List<Row>
)