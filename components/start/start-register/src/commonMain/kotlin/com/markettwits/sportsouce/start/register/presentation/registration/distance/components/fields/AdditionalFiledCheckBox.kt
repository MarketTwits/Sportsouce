package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFiledCheckBox(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
) {
    val isChecked = field.answer.bool ?: false
    SauceCheckboxRow(modifier = modifier, checked = isChecked,
        onCheckedChange = { onChange(isChecked, field, onFieldChanged) },
        label = { AdditionalFiledTitle(field = field.field) })
    Spacer(modifier = Modifier.height(8.dp))
}

private fun onChange(
    isChecked: Boolean,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
) {
    onFieldChanged(
        field.copy(
            answer = field.answer.copy(
                bool = !isChecked,
            )
        )
    )
}

