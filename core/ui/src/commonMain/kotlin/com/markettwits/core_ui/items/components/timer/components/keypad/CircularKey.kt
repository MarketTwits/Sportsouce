package com.markettwits.core_ui.items.components.timer.components.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.timer.domain.Keypad

/**
 * Created by Saurabh
 */
@Composable
internal fun CircularKey(
    key: Keypad,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    icon: ImageVector? = null,
    onClick: (Keypad) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .clickable {
                onClick(key)
            }
            .then(modifier),
    ) {
        if (icon != null) {
            Icon(
                icon,
                modifier = Modifier.size(22.dp),
                contentDescription = "Delete",
                tint = textColor
            )
        } else {
            Text(
                key.value,
                style = TextStyle(
                    color = textColor,
                    fontSize = 34.sp,
                ),
            )
        }
    }
}
