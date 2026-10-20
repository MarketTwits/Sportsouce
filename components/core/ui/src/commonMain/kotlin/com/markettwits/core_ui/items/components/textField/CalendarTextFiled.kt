package com.markettwits.core_ui.items.components.textField

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * [value] can explicitly control a custom field slot. Existing OutlinedTextFieldBase slots provide
 * their committed value automatically; opening/cancelling never sends a value change.
 */
@OptIn(ExperimentalTime::class)
@Composable
fun CalendarTextFiled(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String? = null,
    clearable: Boolean = false,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit,
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var confirmedDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var initialDate by rememberSaveable { mutableStateOf<Long?>(null) }
    val fieldValue = remember { SelectorFieldValue() }
    val yearRange = 1925..Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    LaunchedEffect(enabled) { if (!enabled) openDialog = false }

    if (openDialog && enabled) CalendarDateDialog(
        initialSelectedDateMillis = initialDate,
        yearRange = yearRange,
        clearable = clearable,
        onDismiss = { openDialog = false },
        onConfirm = { selected ->
            confirmedDate = selected
            openDialog = false
            onValueChanged(selected?.let(::calendarDateText).orEmpty())
        },
    )
    SelectorFieldAnchor(modifier = modifier, enabled = enabled, expanded = openDialog && enabled,
        fieldValue = fieldValue,
        onOpen = {
            val currentValue = value ?: fieldValue.value
            initialDate = if (currentValue != null) calendarDateMillis(currentValue, yearRange) else confirmedDate
            openDialog = true
        }) {
        textFiled(Modifier)
    }
}
