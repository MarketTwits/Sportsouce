package com.markettwits.settings.api.api.params

import kotlinx.serialization.Serializable

@Serializable
sealed interface BottomPanel {
    @Serializable
    data object WithLabel : BottomPanel

    @Serializable
    data object WithoutLabel : BottomPanel
}