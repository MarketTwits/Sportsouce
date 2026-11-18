package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

