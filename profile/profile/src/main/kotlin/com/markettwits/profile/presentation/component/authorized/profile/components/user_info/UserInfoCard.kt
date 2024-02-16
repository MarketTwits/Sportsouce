package com.markettwits.profile.presentation.component.authorized.profile.components.user_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun UserInfoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp),
            model = "", contentDescription = ""
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Иван иванов",
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        val status = ""
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = if (status.isEmpty()) "Статус не установлен" else status,
            fontFamily = FontNunito.regular,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Зарегестрирован(а) 21 декабря 2021 года",
            fontFamily = FontNunito.regular,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Button(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = Shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            onClick = {

            }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Редактировать",
                fontSize = 14.sp,
                fontFamily = FontNunito.regular,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        UserSocialNetwork()

    }
}