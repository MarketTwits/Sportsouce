package com.markettwits.sportsouce.start.presentation.result.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberResult(
    val bodyNumber: String,
    val circles: Map<Int, String>,
    val distance: String,
    val group: String,
    val id: Int,
    val name: String,
    val place: Int,
    val result: String,
    val sex: String,
    val shift: String,
    val startId: Int,
    val team: String,
)