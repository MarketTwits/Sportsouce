package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.text.ClickableText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm

@Composable
fun WorkoutRegistrationContent(modifier: Modifier = Modifier, form: WorkoutRegistrationForm,
    onValueChanged: (WorkoutRegistrationForm) -> Unit, isLoading: Boolean, errorMessage: String,
    onClickRegister: () -> Unit, onPhoneClick: (String) -> Unit, onLinkClick: (String) -> Unit) {
    Column(modifier.fillMaxWidth().imePadding().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Оставьте контакты — мы свяжемся с вами и поможем выбрать группу.",
            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        WorkoutRegistrationType(registrationType = form.type)
        WorkoutRegistrationFieldsContent(form = form, onValueChanged = { if (!isLoading) onValueChanged(it) })
        if (errorMessage.isNotBlank()) Text(errorMessage, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.danger.content)
        SauceButton("Записаться", onClickRegister, modifier = Modifier.fillMaxWidth(), enabled = !isLoading, loading = isLoading)
        ClickableText(text = "Вы можете позвонить +7 968 22 111 22 или написать нам https://t.me/sportsoyuznsk",
            onPhoneClick = onPhoneClick, onLinkClick = onLinkClick,
            style = MaterialTheme.typography.bodySmall.copy(color = SauceTheme.colors.textSecondary))
    }
}
