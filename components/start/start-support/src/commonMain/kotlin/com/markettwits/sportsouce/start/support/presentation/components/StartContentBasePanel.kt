package com.markettwits.sportsouce.start.support.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore

@Composable
internal fun StartSupportPanel(
    modifier: Modifier = Modifier,
    state: StartSupportStore.State,
    onRetryCheck: () -> Unit,
    onConfirmDonation: () -> Unit,
    onPayRegistration: () -> Unit,
    onSignIn: () -> Unit,
    onValueChanged: (String) -> Unit,
    onClickSupport: () -> Unit
) {
    val focus = LocalFocusManager.current
    Column(modifier = modifier.fillMaxWidth().sauceVerticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp).padding(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StartSupportInfoCard()
        if (state.isError) Text(state.message, style = MaterialTheme.typography.bodyMedium,
            color = SauceTheme.colors.danger.content, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        val check = (state.checkState as? StartSupportStore.CheckState.Ready)?.result
        when (state.checkState) {
            StartSupportStore.CheckState.Idle, StartSupportStore.CheckState.Loading -> {
                Row(Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(Modifier.size(20.dp), color = SauceTheme.colors.action, strokeWidth = 2.dp)
                    Text("Проверяем ваши регистрации…", Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                }
            }
            StartSupportStore.CheckState.Failed -> {
                Text("Не удалось проверить регистрацию. Повторите проверку, прежде чем отправлять пожертвование.",
                    style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.danger.content)
                SauceButton("Повторить проверку", onRetryCheck, variant = SauceButtonVariant.Outlined)
            }
            StartSupportStore.CheckState.AuthorizationRequired -> {
                Text("Войдите в аккаунт", modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                Text("Чтобы проверить ваши регистрации и не перепутать пожертвование с оплатой участия, необходимо авторизоваться.",
                    modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary)
                SauceButton("Войти", onSignIn)
            }
            is StartSupportStore.CheckState.Ready -> if (check != null && check.requiresConfirmation) {
                val amount = (check.registration?.amountKopecks ?: check.matchedAmountKopecks)?.let(::formatDonationAmount)
                Surface(shape = SauceShapes.field, color = SauceTheme.colors.warning.container) {
                    Column(Modifier.fillMaxWidth().padding(16.dp).semantics { liveRegion = LiveRegionMode.Polite },
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (check.hasUnpaidRegistration) "У вас есть неоплаченная регистрация на этот старт" +
                            (amount?.let { " — $it" } ?: "") else "Проверьте оплату участия",
                            modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall,
                            color = SauceTheme.colors.warning.content)
                        Text("Пожертвование не оплачивает участие. Чтобы попасть в списки участников, оплатите регистрацию.",
                            modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium,
                            color = SauceTheme.colors.textPrimary)
                    }
                }
                if (check.hasUnpaidRegistration && check.registration?.canPay == true && check.registration.groupId > 0) {
                    SauceButton("Оплатить регистрацию", onPayRegistration, enabled = !state.isLoading, loading = state.isLoading)
                }
                if (!state.donationConfirmed) {
                    SauceButton("Всё равно поддержать проект", onConfirmDonation,
                        variant = SauceButtonVariant.Text, enabled = !state.isLoading)
                }
            }
        }
        if (check == null || (check.requiresConfirmation && !state.donationConfirmed)) return@Column
        OutlinedTextFieldBase(
            label = "Сумма",
            value = state.cost,
            isEnabled = !state.isLoading,
            isError = state.amountError != null,
            errorMessage = state.amountError,
            visualTransformation = RubleSymbolVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        ) {
            if (it.length < 7 && it.all(Char::isDigit)) {
                onValueChanged(it)
            }
        }
        SauceButton(
            enabled = !state.isLoading && state.cost.isNotBlank(),
            loading = state.isLoading,
            title = "Поддержать",
            onClick = {
                onClickSupport()
                focus.clearFocus()
            }
        )
    }
}

internal fun formatDonationAmount(kopecks: Long): String {
    val rubles = kopecks / 100
    val fraction = (kopecks % 100).toString().padStart(2, '0')
    return if (kopecks % 100 == 0L) "$rubles ₽" else "$rubles,$fraction ₽"
}
