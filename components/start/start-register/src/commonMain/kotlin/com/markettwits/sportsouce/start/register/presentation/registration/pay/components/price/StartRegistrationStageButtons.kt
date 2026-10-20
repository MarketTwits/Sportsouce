package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.start.register.presentation.registration.common.components.RegistrationActions
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage

@Composable
fun StartRegistrationStagePage.Registration.ButtonContent(
    modifier: Modifier = Modifier,
    onClickGoBack: () -> Unit,
    onClickGoNext: () -> Unit,
) {
    RegistrationActions(
        modifier = modifier,
        primaryTitle = if (isGoNextAvailable) "Вперёд" else null,
        onPrimaryClick = onClickGoNext,
        onBackClick = if (isGoBackAvailable) onClickGoBack else null,
    )
}

@Composable
fun StartRegistrationStagePage.Pay.ButtonContent(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onClickGoBack: () -> Unit,
    onClickPay: () -> Unit,
    onClickSave: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (startInfo.regType is StartRegType.ReReg) {
            ReRegistrationInfoBanner(modifier = Modifier.padding(horizontal = SauceSpacing.large))
        }
        RegistrationActions(
            primaryTitle = when (price) {
                StartRegistrationPriceResult.Empty -> null
                StartRegistrationPriceResult.Free -> "Зарегистрироваться"
                is StartRegistrationPriceResult.Value -> "Оплатить"
            },
            onPrimaryClick = if (price is StartRegistrationPriceResult.Free) onClickSave else onClickPay,
            secondaryTitle = if (price is StartRegistrationPriceResult.Value) "Сохранить" else null,
            onSecondaryClick = onClickSave,
            onBackClick = onClickGoBack,
            loading = loading,
        )
    }
}

@Composable
private fun ReRegistrationInfoBanner(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SauceTheme.colors.warning.container
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SauceTheme.colors.warning.content
        ),
        shape = Shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Sync,
                contentDescription = "Перерегистрация",
                tint = SauceTheme.colors.warning.content
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Перерегистрация",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Вы изменяете свою текущую регистрацию",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary
                )
            }
        }
    }
}
