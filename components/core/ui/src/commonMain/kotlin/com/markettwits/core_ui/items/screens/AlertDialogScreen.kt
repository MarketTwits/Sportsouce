package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AlertDialogScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickOk: () -> Unit,
    text: String = DEFAULT_TEXT,
    title: String = DEFAULT_TITLE,
) {
    val colors = SauceTheme.colors
    BasicAlertDialog(modifier = modifier.padding(24.dp), onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(modifier = Modifier.fillMaxWidth(), shape = SauceShapes.card, color = colors.surfaceRaised, contentColor = colors.textPrimary) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Title and body share the available viewport; neither can push actions off screen.
                Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = colors.warning.content,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(title, Modifier.fillMaxWidth().semantics { heading() },
                        style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                    Text(text, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge,
                        color = colors.textSecondary)
                }
                FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SauceButton(title = "Нет", variant = SauceButtonVariant.Text, fillWidth = false, onClick = onDismissRequest)
                    SauceButton(title = "Да", variant = SauceButtonVariant.Destructive, fillWidth = false, onClick = onClickOk)
                }
            }
        }
    }
}

private const val DEFAULT_TITLE = "Вы действительно хотите выйти?"
private const val DEFAULT_TEXT = "Введённые изменения не сохранятся"
