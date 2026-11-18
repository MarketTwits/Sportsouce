package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldSingleSelect(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {

    val options = field.field.options.map { option ->
        val title = option.price?.takeIf { it != 0 }?.let { "${option.title} (${it.formatPrice()} ₽)" } ?: option.title
        AdditionalFieldOption(option.id, title)
    }
    val selection = options.find { it.id == field.answer.singleSelect }
    DropDownSpinner(
        modifier = modifier.fillMaxWidth(),
        itemList = options,
        selectedItem = selection,
        onItemSelected = { index, _ ->
            onFieldChanged(field.copy(answer = field.answer.copy(singleSelect = options[index].id)))
        },
    ) {
        AdditionalFieldInput(
            field = field.field,
            value = selection?.title.orEmpty(),
            placeholder = "Выберите вариант",
        )
    }
}

private data class AdditionalFieldOption(val id: Int, val title: String) {
    override fun toString(): String = title
}
