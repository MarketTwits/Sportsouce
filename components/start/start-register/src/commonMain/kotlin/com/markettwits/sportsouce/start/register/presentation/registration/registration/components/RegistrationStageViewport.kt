package com.markettwits.sportsouce.start.register.presentation.registration.registration.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Placed inside Decompose's child saveable-state scope, so stages keep independent scroll positions. */
@Composable
internal fun RegistrationStageViewport(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), content = content)
}
