package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    OnBackgroundCard(modifier = modifier) {
        Text(
            modifier = it.padding(start = 20.dp, top = 20.dp),
            text = "Вы действительно хотите выйти из аккаунта ?",
            fontFamily = FontNunito.bold(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Row {
            ButtonContentBase(
                modifier = it
                    .weight(1f)
                    .height(35.dp),
                title = "Выйти",
                containerColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary,
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
