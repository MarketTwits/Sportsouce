package com.markettwits.settings.api.api.params

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationSettings(
    val theme: ColorTheme = ColorTheme.System,
    val bottomPanel: BottomPanel = BottomPanel.WithLabel
)