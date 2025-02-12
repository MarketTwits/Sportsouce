package com.markettwits.core_ui.items.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_extensions.noRippleClickable

@Composable
fun OnBackgroundCard(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.(Modifier) -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(3.dp, shape = shape)
            .noRippleClickable { }
            .clickable(
                indication = if (onClick == null) null else LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }) {
                if (onClick != null) {
                    onClick()
                }
            }
            .fillMaxWidth(),
        shape = shape,
        colors = colors
    ) {
        content(modifier)
    }
}
