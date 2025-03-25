package com.markettwits.sportsouce.shop.order.presentation.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopCreateOrderButton(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    isAvailable : Boolean,
    message : String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(10.dp)
            .height(55.dp)
            .fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
        enabled = !isLoading && isAvailable,
        shape = Shapes.medium,
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    strokeCap = StrokeCap.Round
                )
            } else {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = message,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                )
            }
        }
    )
}
