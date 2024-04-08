package com.markettwits.registrations.registrations_list.presentation.components.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun RegistrationsFilterItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    value: String,
    onClick: () -> Unit
) {
    val borderColor =
        if (checked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
    val containerColor =
        if (checked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val fontColor =
        if (checked) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = Shapes.medium,
        border = BorderStroke(0.5.dp, borderColor),
        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 10.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = fontColor
        )
    }
}