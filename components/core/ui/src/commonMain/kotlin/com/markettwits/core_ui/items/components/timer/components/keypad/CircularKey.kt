package com.markettwits.core_ui.items.components.timer.components.keypad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline

@Composable
internal fun CircularKey(
    key: Keypad,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SauceTheme.colors.surfaceSubtle,
    textColor: Color = SauceTheme.colors.textPrimary,
    icon: ImageVector? = null,
    onClick: (Keypad) -> Unit,
) {
    val interactions = remember { MutableInteractionSource() }
    Surface(
        modifier = modifier.heightIn(min = 56.dp)
            .sauceFocusOutline(interactions, CircleShape, SauceTheme.colors.action, true)
            .semantics { role = Role.Button },
        interactionSource = interactions,
        shape = CircleShape,
        color = backgroundColor,
        contentColor = textColor,
        onClick = { onClick(key) },
    ) {
        Box(Modifier.padding(horizontal = 8.dp, vertical = 12.dp), contentAlignment = Alignment.Center) {
            if (icon != null) Icon(icon, contentDescription = "Удалить цифру", modifier = Modifier.size(24.dp))
            else Text(key.value, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center)
        }
    }
}
