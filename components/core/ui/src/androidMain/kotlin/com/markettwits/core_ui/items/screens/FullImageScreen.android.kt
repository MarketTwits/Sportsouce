package com.markettwits.core_ui.items.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun SauceDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties,
    content: @Composable (() -> Unit),
) {
    Dialog(
        onDismissRequest = onDismissRequest, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ), content = content
    )
}