package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage

@Composable
fun StartRegistrationStagePage.Registration.ButtonContent(
    modifier: Modifier = Modifier,
    onClickGoBack: () -> Unit,
    onClickGoNext: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isGoBackAvailable) {
            StartRegistrationStageButton(
                modifier = Modifier.weight(1f),
                title = "Назад",
                onClick = onClickGoBack
            )
        }
        if (isGoNextAvailable) {
            StartRegistrationStageButton(
                modifier = Modifier.weight(1f),
                title = "Вперед",
                onClick = onClickGoNext
            )
        }
    }
}

@Composable
fun StartRegistrationStagePage.Pay.ButtonContent(
    modifier: Modifier = Modifier,
    onClickGoBack: () -> Unit,
    onClickPay: () -> Unit,
    onClickSave: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StartRegistrationStageButton(
            modifier = Modifier.weight(1f),
            title = "Назад",
            onClick = onClickGoBack
        )
        when (price) {
            is StartRegistrationPriceResult.Empty -> {

            }

            is StartRegistrationPriceResult.Free -> {
                StartRegistrationStageButton(
                    modifier = Modifier.weight(1f),
                    title = "Зарегистрироваться",
                    onClick = onClickSave
                )
            }

            is StartRegistrationPriceResult.Value -> {
                StartRegistrationStageButton(
                    modifier = Modifier.weight(1f),
                    title = "Оплатить",
                    onClick = onClickPay
                )

                StartRegistrationStageButton(
                    modifier = Modifier.weight(1f),
                    title = "Сохранить",
                    onClick = onClickSave
                )
            }
        }
    }
}

@Composable
private fun StartRegistrationStageButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    ButtonContentBase(
        modifier = modifier.padding(4.dp),
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = Shapes.medium,
        textColor = MaterialTheme.colorScheme.onSecondary,
        title = title,
        onClick = onClick
    )
}