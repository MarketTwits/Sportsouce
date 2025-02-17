package com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start_cloud.model.start.fields.Option

@Composable
internal fun AdditionalFieldSingleSelect(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {

    val options by remember {
        mutableStateOf(field.field.options)
    }

    val selectedOption = field.field.options.find { it.id == field.answer.singleSelect }


    Column(modifier = modifier.fillMaxWidth()) {

        StartRegistrationAdditionalFiledTitle(field = field.field)

        val visibleList = options.map {
            if (it.price != null)
                "${it.title} Цена: (${it.price.formatPrice()} ₽)"
            else it.title
        }

        DropDownSpinner(
            itemList = visibleList,
            defaultText = "",
            selectedItem = field.answer.singleSelect,
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
                OutlinedTextFieldBase(
                    label = "",
                    value = selectedOption?.title ?: "",
                    isEnabled = false
                ) {}
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}