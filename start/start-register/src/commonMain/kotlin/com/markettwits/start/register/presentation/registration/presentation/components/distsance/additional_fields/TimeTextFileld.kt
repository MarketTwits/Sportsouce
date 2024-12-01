package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTextField(
    modifier: Modifier = Modifier,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (Int,Int, Int) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    if (openDialog) {
        TimePickerDialog(
            onDismiss = {
                openDialog = false
            }, onConfirm = {
                openDialog = false
            },
            content = {

                TimeInput(
                    state = timePickerState,
                )


//                TimePicker(
//                    onTimeSelected = { hour, minute, second ->
//                        onValueChanged(hour, minute, second)
//                    }
//                )
            }
        )
    }
    textFiled(modifier.clickable {
        openDialog = true
    })
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun sportSouceTimePickerColors(): TimePickerColors = TimePickerDefaults.colors(
    containerColor = MaterialTheme.colorScheme.primary,
)