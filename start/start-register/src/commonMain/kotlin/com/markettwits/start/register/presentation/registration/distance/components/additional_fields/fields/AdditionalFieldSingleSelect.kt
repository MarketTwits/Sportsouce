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
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import kotlinx.collections.immutable.toImmutableList

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

        DropDownSpinner(
            itemList = options
                .map { it.title }
                .toImmutableList(),
            selectedItem = field.answer.singleSelect,
            onItemSelected = { id, item ->
                onFieldChanged(
                    field.copy(
                        answer = field.answer.copy(
                            singleSelect = options.find { it.title == item }?.id,
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