package com.markettwits.core_ui.items.components.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    skipPartiallyExpanded : Boolean = true,
    title: String? = null,
    dragHandle: (@Composable () -> Unit)? = null,
    edgeToEdgeContent: Boolean = false,
    dismissEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val canDismiss by rememberUpdatedState(dismissEnabled)
    val dismiss = { if (canDismiss) onDismissRequest() }
    SauceModalBottomSheet(
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded) { value ->
            value != SheetValue.Hidden || canDismiss
        },
        containerColor = SauceTheme.colors.surface,
        contentColor = SauceTheme.colors.textPrimary,
        shape = SauceShapes.sheet,
        tonalElevation = 0.dp,
        scrimColor = SauceTheme.colors.material.scrim.copy(alpha = 0.42f),
        dragHandle = dragHandle ?: { DefaultDragHandle() },
        onDismissRequest = dismiss,
        edgeToEdgeContent = edgeToEdgeContent,
    ) {
        if (dragHandle == null) SheetHeader(dismiss = dismiss, title = title)
        ProvideTextStyle(MaterialTheme.typography.bodyLarge) { content() }
    }
}
