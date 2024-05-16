package com.markettwits.club.dashboard.presentation.dashboard.components.title

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MainDashboardContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        MainDashboardImage()
        MainDashboardTitle()
    }
}