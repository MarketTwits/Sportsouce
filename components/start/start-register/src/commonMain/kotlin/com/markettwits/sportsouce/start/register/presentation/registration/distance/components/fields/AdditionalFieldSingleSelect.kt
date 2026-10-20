package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer

@Composable
internal fun AdditionalFieldSingleSelect(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {

    val options = field.field.options

    val selectedOption = field.field.options.find { it.id == field.answer.singleSelect }


    Column(modifier = modifier.fillMaxWidth()) {

        AdditionalFiledTitle(field = field.field)

        val visibleList = options.map { option ->
            AdditionalFieldOption(option.id, if (option.price != null)
                "${option.title} Цена: (${option.price.formatPrice()} ₽)" else option.title)
        }

        DropDownSpinner(
            itemList = visibleList,
            selectedItem = visibleList.find { it.id == field.answer.singleSelect },
            onItemSelected = { id, item ->
                onFieldChanged(
                    field.copy(
                        answer = field.answer.copy(
                            singleSelect = options.getOrNull(id)?.id,
                        )
                    )
                )
            },
            textFiled = {
                val value = buildString {
                    append(selectedOption?.title.orEmpty())

                    selectedOption?.price
                        ?.takeIf { it != 0 }
                        ?.let { append(" (${it.formatPrice()} ₽)") }
                }
                OutlinedTextFieldBase(
                    label = "",
                    value = value,
                    isEnabled = false
                ) {}
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
private data class AdditionalFieldOption(val id: Int, val title: String) {
    override fun toString(): String = title
}
