package com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start_cloud.model.start.fields.Option

@Composable
internal fun AdditionalFieldMultiselect(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit,
) {
    val options = field.field.options

    val selectedOptions =  field.answer.multiSelect?.toMutableList() ?: emptyList<Int>().toMutableList()

    Column(modifier = modifier.fillMaxWidth()) {
        StartRegistrationAdditionalFiledTitle(field = field.field)
        options.forEach { option ->
            val isSelected = selectedOptions.contains(option.id)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onChange(isSelected, option, field, selectedOptions, onFieldChanged)
                    }
                    .fillMaxWidth()
            ) {
                CheckBoxBase(
                    checked = isSelected,
                    onValueChanged = {
                        onChange(isSelected, option, field, selectedOptions, onFieldChanged)
                    }
                )
                val text = if (option.price != null )
                    "${option.title} + (${option.price.formatPrice()} ₽)"
                else
                    option.title
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
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