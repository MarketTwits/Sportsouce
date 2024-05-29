package com.markettwits.settings.api.api.params

import kotlinx.serialization.Serializable

@Serializable
data class BottomPanel(val isCompact: Boolean = false)