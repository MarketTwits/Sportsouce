package com.markettwits.registrations.registrations.presentation.components.starts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun RegistrationsEmpty(modifier : Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()){
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