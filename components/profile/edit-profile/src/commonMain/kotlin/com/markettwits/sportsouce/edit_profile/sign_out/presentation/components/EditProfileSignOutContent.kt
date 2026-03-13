package com.markettwits.sportsouce.edit_profile.sign_out.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito


@Composable
fun EditProfileSignOutContent(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
    apply: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                modifier = it.padding(start = 8.dp, top = 8.dp),
                text = "Вы действительно хотите выйти из аккаунта ?",
                fontFamily = FontNunito.medium(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonContentBase(
                    modifier = it
                        .weight(1f)
                        .height(35.dp),
                    title = "Выйти",
                    containerColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onError,
                    onClick = { apply() },
                )
                Spacer(modifier = Modifier.padding(5.dp))
                ButtonContentBase(
                    modifier = it
                        .weight(1f)
                        .height(35.dp),
                    title = "Отмена",
                    borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    onClick = { dismiss() }
                )
            }
        }
    }
}
