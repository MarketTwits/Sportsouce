package com.markettwits.core_ui.items.components.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
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
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded),
        containerColor = MaterialTheme.colorScheme.primary,
        dragHandle = dragHandle,
        onDismissRequest = onDismissRequest,
        content = content
    )
}