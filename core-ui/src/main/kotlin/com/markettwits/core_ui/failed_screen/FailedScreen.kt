package com.markettwits.core_ui.failed_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito


@Composable
fun FailedScreen(
    modifier: Modifier = Modifier.padding(5.dp),
    title: String = "Ой что-то пошло не так...",
    message: String = "Произошла ошибка при загрузке данных",
    onClickHelp: () -> Unit,
    onClickRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .align(Alignment.Center)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier,
                text = title,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            Text(
                modifier = modifier,
                text = message,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.regular,
                fontSize = 12.sp
            )
            Row(modifier = modifier) {
                OutlinedButton(
                    modifier = modifier,
                    onClick = {
                        onClickHelp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(
                        text = "Помощь",
                        color = Color.Gray,
                        fontFamily = FontNunito.regular,
                        fontSize = 12.sp
                    )
                }
                OutlinedButton(
                    modifier = modifier,
                    onClick = {
                        onClickRetry()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Повторить",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FailedScreenPreview() {
    FailedScreen(
        onClickHelp = {

    }, onClickRetry = {

    })
}