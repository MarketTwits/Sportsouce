package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.sportsouce.start.cloud.model.start.fields.Option
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldMultiselect(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
) {
    val options = field.field.options

    val selectedOptions =  field.answer.multiSelect?.toMutableList() ?: emptyList<Int>().toMutableList()

    Column(modifier = modifier.fillMaxWidth()) {
        AdditionalFiledTitle(
            modifier = Modifier.padding(vertical = 4.dp),
            field = field.field
        )
        options.forEach { option ->
            val isSelected = selectedOptions.contains(option.id)
            val text = if (option.price != null)
                "${option.title} + (${option.price.formatPrice()} ₽)"
            else option.title
            SauceCheckboxRow(label = text, checked = isSelected, onCheckedChange = {
                onChange(isSelected, option, field, selectedOptions, onFieldChanged)
            })
        }
    }
}

private fun onChange(
    isSelected : Boolean,
    option: Option,
    field: StartRegistrationStatementAnswer,
    selectedOptions : MutableList<Int>,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
){
    if (isSelected) {
        selectedOptions.remove(option.id)
    } else {
        selectedOptions.add(option.id)
    }
    onFieldChanged(
        field.copy(
            answer = field.answer.copy(
                multiSelect = selectedOptions,
            )
        )
    )
}