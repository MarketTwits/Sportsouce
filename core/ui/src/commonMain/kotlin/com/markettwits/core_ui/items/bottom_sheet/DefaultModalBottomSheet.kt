package com.markettwits.core_ui.items.bottom_sheet

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    skipPartiallyExpanded : Boolean = true,
    dragHandle: @Composable () -> Unit = { ClosableDragHandle(dismiss = onDismissRequest) },
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier
            .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded),
        containerColor = MaterialTheme.colorScheme.primary,
        dragHandle = dragHandle,
        onDismissRequest = onDismissRequest,
        content = content
    )
}