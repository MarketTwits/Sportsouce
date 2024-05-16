package com.markettwits.club.registration.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun WorkoutRegistrationContent(
    modifier: Modifier = Modifier,
    form: WorkoutRegistrationForm,
    onValueChanged: (WorkoutRegistrationForm) -> Unit,
    isLoading: Boolean,
    errorMessage: String,
    onClickRegister: () -> Unit,
    onPhoneClick: (String) -> Unit,
    onLinkClick: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = "Заполните форму и мы вам перезвоним ♥",
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Start,
            fontFamily = FontNunito.bold(),
            fontSize = 20.sp
        )
        ClickableTextWrapper(
            modifier = Modifier.padding(4.dp),
            onPhoneClick = onPhoneClick,
            onLinkClick = onLinkClick
        )
        WorkoutRegistrationFieldsContent(
            modifier = Modifier.padding(4.dp),
            form = form,
            onValueChanged = onValueChanged
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Попробуйте ещё раз: $errorMessage",
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Start,
                fontFamily = FontNunito.bold(),
                fontSize = 14.sp
            )
        }
        ButtonContentBase(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            textColor = MaterialTheme.colorScheme.onSecondary,
            title = "Записаться",
            onClick = onClickRegister,
            isEnabled = !isLoading,
            showContent = isLoading,
            content = {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    strokeCap = StrokeCap.Round
                )
            }
        )
    }
}