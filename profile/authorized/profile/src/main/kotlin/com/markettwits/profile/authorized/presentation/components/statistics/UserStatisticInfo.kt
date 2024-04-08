package com.markettwits.profile.authorized.presentation.components.statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.theme.FontNunito

@Composable
internal fun UserStatisticInfo(
    modifier: Modifier = Modifier,
    onClickOk: () -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        Column(modifier = it.padding(14.dp)) {
            Text(
                text = "Информация",
                fontFamily = FontNunito.bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = message,
                fontFamily = FontNunito.medium,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            ButtonContentBase(
                title = "Понятно",
                onClick = { onClickOk() },
                borderStroke = BorderStroke(0.1.dp, MaterialTheme.colorScheme.outline)
            )
        }
    }
}

private const val message =
    "Динамика просмотров расчитывается для оплаченных стартов за последние 2 года"