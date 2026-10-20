package com.markettwits.core_ui.items.components.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun DefaultDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 32.dp,
    height: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = SauceTheme.colors.textMuted.copy(alpha = 0.4f),
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(width, height).background(color, shape))
        }
        content?.invoke(this)
    }
}

@Composable
fun ClosableDragHandle(modifier: Modifier = Modifier, dismiss: () -> Unit, title: String? = null) {
    if (title != null) {
        Column(modifier.fillMaxWidth()) {
            DefaultDragHandle()
            SheetHeader(dismiss = dismiss, title = title)
        }
    } else {
        Box(modifier.fillMaxWidth().height(48.dp).padding(horizontal = 8.dp)) {
            Box(Modifier.align(Alignment.TopCenter).padding(top = 12.dp).size(32.dp, 4.dp)
                .background(SauceTheme.colors.textMuted.copy(alpha = 0.4f), MaterialTheme.shapes.extraLarge))
            Box(Modifier.align(Alignment.CenterEnd)) { SheetCloseButton(dismiss) }
        }
    }
}

/** Kept outside Material's clickable drag handle so tapping a heading never dismisses the sheet. */
@Composable
internal fun SheetHeader(dismiss: () -> Unit, title: String?) {
    Row(
        Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title == null) Spacer(Modifier.weight(1f))
        else Text(title, Modifier.weight(1f).semantics { heading() },
            style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
        SheetCloseButton(dismiss)
    }
}

@Composable
private fun SheetCloseButton(dismiss: () -> Unit) {
    IconButton(onClick = dismiss, modifier = Modifier.size(48.dp)) {
        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = SauceTheme.colors.textSecondary,
            modifier = Modifier.size(20.dp))
    }
}
