package com.markettwits.core_ui.items.components.topbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBarWithClip(
    modifier: Modifier = Modifier,
    title: String,
    isStatusBarHandle: Boolean = true,
    goBack: () -> Unit,
) {
    TopBarBase(
        modifier = modifier,
        title = title,
        isStatusBarHandle = isStatusBarHandle,
        goBack = goBack,
    )
}