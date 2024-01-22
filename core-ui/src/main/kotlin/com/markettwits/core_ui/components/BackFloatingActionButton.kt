package com.markettwits.core_ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun BackFloatingActionButton(back : () -> Unit) {
    SmallFloatingActionButton(
        modifier = Modifier.padding(10.dp),
        containerColor = Color.White,
        contentColor = SportSouceColor.SportSouceBlue,
        onClick = {
            back()
        },
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Floating action button."
        )
    }
}