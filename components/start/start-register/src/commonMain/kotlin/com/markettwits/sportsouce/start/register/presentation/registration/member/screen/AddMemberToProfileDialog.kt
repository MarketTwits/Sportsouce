package com.markettwits.sportsouce.start.register.presentation.registration.member.screen

import androidx.compose.foundation.selection.selectableGroup
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidEmail
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidPhone

@Composable
internal fun AddMemberToProfileDialog(
    relationType: String,
    email: String,
    phone: String,
    shouldRequestContacts: Boolean,
    showValidation: Boolean,
    dialogErrorMessage: String?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onRelationTypeChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    val relationTypes = listOf(
        RelationTypeUi(value = "Родственник", title = "Родственник"),
        RelationTypeUi(value = "Друг", title = "Друг"),
        RelationTypeUi(value = "Партнер по команде", title = "Товарищ по команде")
    )
    val isEmailInvalid = showValidation && !email.trim().isValidEmail()
    val isPhoneInvalid = showValidation && !phone.trim().isValidPhone()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primary,
        title = {
            Text(
                text = "Добавить участника в профиль",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold()
            )
        },
        text = {
            Column {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Заполните недостающие данные",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontFamily = FontNunito.medium()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Кем приходится участник?",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.semiBoldBold()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(Modifier.selectableGroup()) {
                    relationTypes.forEach { item ->
                        SauceRadioRow(label = item.title, selected = relationType == item.value,
                            onClick = { onRelationTypeChanged(item.value) })
                    }
                }

                if (!dialogErrorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dialogErrorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontNunito.semiBoldBold()
                    )
                }

                if (shouldRequestContacts) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Для сохранения в профиль заполните контакты",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.semiBoldBold()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextFieldBase(
                        label = "Почта",
                        value = email,
                        isError = isEmailInvalid,
                        supportingText = if (isEmailInvalid) {
                            {
                                Text(
                                    text = "Введите корректный email",
                                    color = MaterialTheme.colorScheme.error,
                                    fontFamily = FontNunito.medium()
                                )
                            }
                        } else null,
                        onValueChange = onEmailChanged
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinePhoneTextFiled(
                        label = "Номер телефона",
                        value = phone,
                        isError = isPhoneInvalid,
                        supportingText = if (isPhoneInvalid) {
                            {
                                Text(
                                    text = "Введите корректный номер телефона",
                                    color = MaterialTheme.colorScheme.error,
                                    fontFamily = FontNunito.medium()
                                )
                            }
                        } else null,
                        onValueChange = onPhoneChanged
                    )
                }
            }
        },
        confirmButton = {
            SauceButton("Добавить", onConfirm, fillWidth = false,
                enabled = !isLoading, loading = isLoading)
        },
        dismissButton = {
            SauceButton("Отмена", onDismiss, fillWidth = false,
                variant = SauceButtonVariant.Text, enabled = !isLoading)
        },
    )
}

private data class RelationTypeUi(
    val value: String,
    val title: String,
)
