package com.markettwits.sportsouce.start.register.presentation.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.screens.AdaptivePane
import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.presentation.success.icon.SuccessPaymantIc

@Composable
fun RegisterSuccessScreen(
    component: RegisterSuccessComponent
) {
    AdaptivePane(modifier = Modifier.safeDrawingPadding()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
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
                color = SauceTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            Text(
                text = "Вы можете оплатить участие в личном кабинете",
                color = SauceTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(14.dp))
            SauceButton(
                title = "Продолжить",
                onClick = {
                    component.onClickContinue()
                }
            )
        }
    }
    }
}
