package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer

@Composable
fun StartRegistrationAdditionalFieldCheckBox(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    val isChecked = field.answer.bool ?: false
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onChange(isChecked, field,onFieldChanged)
                }
        ) {
            CheckBoxBase(
                checked = isChecked,
                onValueChanged = {
                    onChange(isChecked, field,onFieldChanged)
                }
            )
            StartRegistrationAdditionalFiledTitle(field = field.field)
        }
    Spacer(modifier = Modifier.height(8.dp))
}

private fun onChange(
    isChecked : Boolean,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
){
    onFieldChanged(
        field.copy(
            answer = field.answer.copy(
                bool = !isChecked,
            )
        )
    )
}

