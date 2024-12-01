package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start_cloud.model.register.price.fields.StartRegisterAnswer

@Composable
internal fun StartRegistrationAdditionalFieldText(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    OutlinedTextFieldBase(
        value = field.answer.string ?: "",
        onValueChange = {
            onFieldChanged(
                field.copy(
                    answer = field.answer.copy(string = it),
                )
            )
        },
        modifier = modifier.fillMaxWidth(),
        label = field.field.title
    )
    Spacer(modifier = Modifier.height(8.dp))
}