package com.markettwits.registrations.registrations_list.presentation.components.starts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun RegistrationsEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = modifier
                .align(Alignment.Center),
            text = "Вы пока не принимали участие в стартах",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun RegistrationsAbsolutelyEmpty(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp),
                text = emptyRegistersTitle,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold,
                lineHeight = 14.sp,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp),
                text = emptyRegistersLabel,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.medium,
                lineHeight = 14.sp,
                fontSize = 12.sp
            )
        }
    }
}

private const val emptyRegistersTitle =
    "Вы пока не принимали участие в стартах"
private const val emptyRegistersLabel =
    "Примите участие в стартах и они появятся именно здесь :)"