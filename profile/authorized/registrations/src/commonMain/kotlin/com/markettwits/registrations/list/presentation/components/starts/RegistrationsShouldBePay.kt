package com.markettwits.registrations.list.presentation.components.starts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun RegistrationsShouldBePay(modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SportSouceColor.SportSouceLightRed
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.Start),
                text = "У вас есть неоплаченные старты, оплатите их до окончания регистрации",
                textAlign = TextAlign.Start,
                color = Color.White,
                fontFamily = FontNunito.medium(),
                fontSize = 16.sp
            )
            ButtonContentBase(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.Start),
                shape = Shapes.medium,
                title = "Оплатить",
                containerColor = Color.White,
                textColor = SportSouceColor.SportSouceLightRed,
                onClick = onClick
            )
        }
    }
}