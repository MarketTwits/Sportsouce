package com.markettwits.edit_profile.edit_profile_about.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.components.progress.CircularProgressIndicatorBase
import com.markettwits.core_ui.components.textField.TextFieldBase
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore
import kotlinx.coroutines.delay

@Composable
fun EditProfileAboutContent(
    state: EditProfileAboutStore.State,
    dismiss: () -> Unit,
    apply: () -> Unit,
    onValueChanged: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = Shapes.medium
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            text = "Изменить статус Обо мне",
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Column {
                TextFieldBase(
                    modifier = Modifier,
                    label = "Обо мне",
                    isError = state.fieldStat.isError,
                    value = state.fieldStat.value,
                    maxLines = 6,
                    minLines = 6,
                    singleLine = false,
                    onValueChange = onValueChanged::invoke
                )
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
                        text = state.message,
                        fontFamily = FontNunito.bold,
                        fontSize = 12.sp,
                        color = if (state.isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        modifier = Modifier

                            .padding(top = 10.dp),
                        text = "${state.fieldStat.value.length} / 120",
                        fontFamily = FontNunito.bold,
                        fontSize = 12.sp,
                        color = if (state.fieldStat.isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            Row {
                ButtonContentBase(
                    modifier = Modifier
                        .weight(1f)
                        .height(35.dp),
                    title = "Сохранить",
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    onClick = { apply() },
                    isEnabled = !state.fieldStat.isError,
                    showContent = state.isLoading,
                    content = {
                        CircularProgressIndicatorBase(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(20.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                )
                Spacer(modifier = Modifier.padding(5.dp))
                ButtonContentBase(
                    modifier = Modifier
                        .weight(1f)
                        .height(35.dp),
                    title = "Отмена",
                    borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    onClick = { dismiss() }
                )
            }
        }
        LaunchedEffect(key1 = state) {
            if (state.isSuccess) {
                delay(800)
                dismiss()
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditProfileAboutScreenContentPreview() {
    SportSouceTheme {
        EditProfileAboutContent(
            onValueChanged = {},
            dismiss = {},
            state = EditProfileAboutStore.State(
                isError = true,
                isLoading = true
//                message = "aGgAwgiAWOgjAOOWg;AIgAWGIWGAWgIO:AWgWAgiojIWGoIWGiJOAWGoAWGIOIOAWjigIOAWGjioIOAWGO"
            ),
            apply = {})
    }
}