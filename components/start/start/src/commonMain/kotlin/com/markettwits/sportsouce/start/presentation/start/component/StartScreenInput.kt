package com.markettwits.sportsouce.start.presentation.start.component

import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.serialization.Serializable

@Serializable
sealed class StartScreenInput {

    @Serializable
    data class ReReg(val orderId: Int, val startId: Int) : StartScreenInput()

    @Serializable
    data class Id(val startId: Int) : StartScreenInput()

    @Serializable
    data class Slug(val slug: String) : StartScreenInput()

    @Serializable
    data class Item(val item: StartsListItem) : StartScreenInput()

}