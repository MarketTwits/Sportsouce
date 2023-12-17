package com.markettwits.start.presentation.start.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor


@Composable
fun StartMembersPanel(modifier: Modifier = Modifier, onClick: OnClick) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Список участников",
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            tint = SportSouceColor.SportSouceBlue
        )
    }
}

typealias OnClick = () -> Unit
