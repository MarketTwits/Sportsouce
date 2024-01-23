package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun RegistrationsEmpty(modifier : Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()){
        Text(
            modifier = modifier
                .align(Alignment.Center),
            text = "Вы пока не принимали участие в стартах",
            textAlign = TextAlign.Center,
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold,
            fontSize = 14.sp
        )
    }
}