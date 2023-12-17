package com.markettwits.start.presentation.start.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartTitle(modifier: Modifier = Modifier, title : String, place : String) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.extraBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            text = place,
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
    }
}