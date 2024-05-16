package com.markettwits.start.register.presentation.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.success.icon.SuccessPaymantIc

@Composable
fun RegisterSuccessScreen(
    component: RegisterSuccessComponent
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = SuccessPaymantIc,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(14.dp))
            Text(
                text = "Вы успешно зарегистрировались на старт",
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            Text(
                text = "Вы можете оплатите участие в личном кабинете",
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            ButtonContentBase(
                title = "Продолжить",
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    component.onClickContinue()
                }
            )
        }
    }
}