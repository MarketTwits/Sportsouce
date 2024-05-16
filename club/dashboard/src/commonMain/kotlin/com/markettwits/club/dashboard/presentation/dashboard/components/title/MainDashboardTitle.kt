package com.markettwits.club.dashboard.presentation.dashboard.components.title

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun MainDashboardTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Спортивный клуб",
            fontSize = 24.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Комплексный подход во всем - от дружной команды до собственных спортивных мероприятий",
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}