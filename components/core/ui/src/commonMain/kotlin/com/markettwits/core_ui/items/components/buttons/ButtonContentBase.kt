package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun ButtonContentBase(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: (() -> Unit?)? = null,
    isEnabled: Boolean = true,
    shape: Shape = Shapes.large,
    maxLines: Int = Int.MAX_VALUE,
    containerColor: Color = Color.Transparent,
    disabledContainerColor: Color = Color.Transparent,
    textColor: Color = MaterialTheme.colorScheme.tertiary,
    borderStroke: BorderStroke? = null,
    content: @Composable() (RowScope.() -> Unit)? = null,
    showContent: Boolean = false,
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor
        ),
        shape = shape,
        border = borderStroke,
        onClick = {
            if (onClick != null) {
                onClick()
            }
        }) {
        if (content == null || !showContent) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                color = textColor
            )
        } else {
            content()
        }
    }
}