package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun WorkoutRegistrationSuccessContent(modifier: Modifier = Modifier, onClickNext: () -> Unit) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(Icons.Outlined.CheckCircle, null, Modifier.align(Alignment.CenterHorizontally).size(48.dp), tint = SauceTheme.colors.success.content)
        Text("Мы получили вашу заявку", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
        Text("С вами свяжутся, подтвердят место в группе и расскажут про оплату.", modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        SauceButton("Продолжить", onClickNext, modifier = Modifier.fillMaxWidth())
    }
}
