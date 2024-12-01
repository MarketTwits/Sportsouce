package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.fields

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields.TimeTextField

@Composable
internal fun StartRegistrationAdditionalFieldTextTime(
    modifier: Modifier = Modifier,
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    var time by remember { mutableStateOf("") }

    StartRegistrationAdditionalFiledTitle(field = field.field)

    TimeTextField(
        onValueChanged = { hour, minute, seconds ->
            time = "$hour:$minute:$seconds"
        },
        textFiled = {
//            OutlinedTextFieldBase(
//                modifier = it,
//                label = field.title,
//                value = time,
//                isEnabled = false,
//                onValueChange = {
//                    onFieldChanged(
//                        StartRegisterAnswer(
//                            string = time,
//                            additionalFieldId = field.id,
//                            isOptional = field.isOptional,
//                            type = field.type.name
//                        )
//                    )
//                }
       //     )
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}