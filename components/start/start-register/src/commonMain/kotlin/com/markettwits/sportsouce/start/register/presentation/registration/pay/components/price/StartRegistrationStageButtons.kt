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
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.register.domain.StartRegType
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (startInfo.regType is StartRegType.ReReg) {
            ReRegistrationInfoBanner(
                modifier = Modifier.padding(10.dp),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
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
}

@Composable
private fun ReRegistrationInfoBanner(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SportSouceColor.SportSouceRegistryCommingSoonYellow.copy(alpha = 0.15f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SportSouceColor.SportSouceRegistryCommingSoonYellow
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
                tint = SportSouceColor.SportSouceRegistryCommingSoonYellow
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Перерегистрация",
                    fontFamily = FontNunito.bold(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Вы изменяете свою текущую регистрацию",
                    fontFamily = FontNunito.regular(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
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
        maxLines = 1,
        textColor = MaterialTheme.colorScheme.onSecondary,
        title = title,
        onClick = onClick
    )
}