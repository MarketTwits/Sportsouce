package com.markettwits.core_ui.items.base_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
fun AlertDialogScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickOk: () -> Unit,
    text: String = DEFAULT_TEXT,
    title: String = DEFAULT_TITLE,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest::invoke,
        containerColor = MaterialTheme.colorScheme.primary,
        text = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = text,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.medium(),
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = "waring",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        dismissButton = {
            ButtonContentBase(
                containerColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary,
                title = "Нет",
                onClick = {
                    onDismissRequest()
                }
            )
        },
        confirmButton = {
            ButtonContentBase(
                containerColor = SportSouceColor.SportSouceLightRed,
                textColor = Color.White,
                title = "Да",
                onClick = {
                    onClickOk()
                })
        },
    )
}

private const val DEFAULT_TITLE = "Вы действительно хотите выйти ?"
private const val DEFAULT_TEXT = "Введенные изменения не сохранятся"