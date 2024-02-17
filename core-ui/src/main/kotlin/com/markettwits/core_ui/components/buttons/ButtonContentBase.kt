package com.markettwits.core_ui.components.buttons

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun ButtonContentBase(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    containerColor: Color = Color.Transparent,
    textColor: Color = MaterialTheme.colorScheme.tertiary,
    borderStroke: BorderStroke? = null
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = shape,
        border = borderStroke,
        onClick = {
            onClick()
        }) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = title,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium,
            color = textColor
        )
    }
}