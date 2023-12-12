package com.markettwits.root.root.bottom_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.markettwits.root.root.RootComponent

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Box(
        modifier
            .clickable {
                onClick()
            }
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = true,
                role = Role.Tab,
            ),
        contentAlignment = Alignment.Center
    ){
        icon()
    }
}