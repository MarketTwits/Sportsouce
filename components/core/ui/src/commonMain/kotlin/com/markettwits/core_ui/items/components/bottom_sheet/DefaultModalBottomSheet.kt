package com.markettwits.core_ui.items.components.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar
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
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded),
        containerColor = SauceTheme.colors.surface,
        contentColor = SauceTheme.colors.textPrimary,
        shape = SauceShapes.sheet,
        tonalElevation = 0.dp,
        scrimColor = SauceTheme.colors.material.scrim.copy(alpha = 0.42f),
        dragHandle = dragHandle ?: { DefaultDragHandle() },
        onDismissRequest = onDismissRequest,
    ) {
        if (dragHandle == null) SheetHeader(dismiss = onDismissRequest, title = title)
        CompositionLocalProvider(LocalNavigationViewport provides null, LocalSauceTopBar provides null) {
            ProvideTextStyle(MaterialTheme.typography.bodyLarge) { content() }
        }
    }
}
