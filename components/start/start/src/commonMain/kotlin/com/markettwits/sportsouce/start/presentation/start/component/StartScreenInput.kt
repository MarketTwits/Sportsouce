package com.markettwits.sportsouce.start.presentation.start.component

import kotlinx.serialization.Serializable

@Serializable
sealed class StartScreenInput {

    @Serializable
    data class Id(val startId: Int) : StartScreenInput()

    @Serializable
    data class Slug(val slug: String) : StartScreenInput()

}