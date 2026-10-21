package com.markettwits.core_ui.items.components.snackbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.semantics
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

enum class SauceSnackbarTone { Neutral, Success, Error }

/** Transient service messages; field validation belongs next to the editor. */
@Composable
fun SauceSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    tone: SauceSnackbarTone = SauceSnackbarTone.Neutral,
) {
    val colors = SauceTheme.colors
    val status = when (tone) {
        SauceSnackbarTone.Neutral -> colors.neutral
        SauceSnackbarTone.Success -> colors.success
        SauceSnackbarTone.Error -> colors.danger
    }
    SnackbarHost(hostState, modifier) { data ->
        Snackbar(
            modifier = Modifier
                .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small)
                .border(1.dp, status.content.copy(alpha = .35f), SauceShapes.field)
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    dismiss { data.dismiss(); true }
                },
            shape = SauceShapes.field,
            containerColor = status.container,
            contentColor = status.content,
            actionContentColor = status.content,
            dismissActionContentColor = status.content,
            action = data.visuals.actionLabel?.let { label -> {
                TextButton(onClick = data::performAction, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(label, color = status.content)
                }
            } },
            dismissAction = if (data.visuals.withDismissAction) {
                {
                    IconButton(onClick = data::dismiss, modifier = Modifier.size(48.dp).clip(SauceShapes.button)) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть сообщение")
                    }
                }
            } else null,
        ) { Text(data.visuals.message) }
    }
}
