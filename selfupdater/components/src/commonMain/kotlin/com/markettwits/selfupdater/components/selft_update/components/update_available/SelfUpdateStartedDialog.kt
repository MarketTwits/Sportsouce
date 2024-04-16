package com.markettwits.selfupdater.components.selft_update.components.update_available

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
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun SelfUpdateStartedDialog(
    message: String,
    result: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        SelfUpdateStarted(
            message = message,
            result = result
        ) {
            onDismiss()
        }
    }
}

@Composable
private fun SelfUpdateStarted(
    message: String,
    result: Boolean,
    modifier: Modifier = Modifier,
    onClickOk: () -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        Column(modifier = it.padding(14.dp)) {
            Text(
                text = "Информация",
                fontFamily = FontNunito.bold(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = if (result) messageSuccess else message,
                fontFamily = FontNunito.medium(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            ButtonContentBase(
                title = "Понятно",
                onClick = { onClickOk() },
                borderStroke = BorderStroke(0.1.dp, MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

private val messageSuccess = "Начата загрузка обновления, дождитесь её и установите приложение." +
        "\n При установке потребуется разрешить установку из неизвестных источников, не бойтесь это абсолютно безопасно :)"