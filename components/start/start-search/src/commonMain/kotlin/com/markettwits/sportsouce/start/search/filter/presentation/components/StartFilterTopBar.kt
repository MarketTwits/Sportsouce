package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun StartFilterTopBar(modifier: Modifier = Modifier, title: String, goBack: () -> Unit) {
    Box(
        modifier
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Icon(
            modifier = modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .noRippleClickable {
                    goBack()
                },
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "back",
            tint = SportSouceColor.SportSouceBlue,
        )
        Text(
            modifier = modifier
                .align(Alignment.Center)
                .padding(start = 30.dp),
            text = title,
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
    }
}