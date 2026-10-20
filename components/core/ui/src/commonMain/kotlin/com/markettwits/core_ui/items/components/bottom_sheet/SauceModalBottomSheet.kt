package com.markettwits.core_ui.items.components.bottom_sheet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.topbar.LocalSauceTopBar

/** Shared modal host, including sheets with a feature-specific header or SheetState. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SauceModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: (@Composable () -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    // For bodies using Sauce scroll components: reserve the safe area in content, not outside the viewport.
    edgeToEdgeContent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        // Reserve the top safe area outside the moving surface. Applying it to the
        // content lets Material's offset-dependent inset consumption change the sheet
        // height, which changes its anchor again during an edge fling.
        modifier = Modifier.windowInsetsPadding(windowInsets.only(WindowInsetsSides.Top)).then(modifier),
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = { windowInsets.only(if (edgeToEdgeContent) WindowInsetsSides.Horizontal
            else WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom) },
        onDismissRequest = onDismissRequest,
    ) {
        CompositionLocalProvider(LocalNavigationViewport provides null, LocalSauceTopBar provides null,
            LocalScrollContentBottomPadding provides if (edgeToEdgeContent) windowInsets.asPaddingValues().calculateBottomPadding() else 0.dp) {
            content()
        }
    }
}
