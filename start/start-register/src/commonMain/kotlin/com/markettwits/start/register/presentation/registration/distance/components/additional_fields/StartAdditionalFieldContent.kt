package com.markettwits.start.register.presentation.registration.distance.components.additional_fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.AdditionalFiledCheckBox
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.AdditionalFieldMultiselect
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.AdditionalFieldNumber
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.AdditionalFieldSingleSelect
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.StartRegistrationAdditionalFieldText
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.StartRegistrationAdditionalFieldTextData
import com.markettwits.start.register.presentation.registration.distance.components.additional_fields.fields.StartRegistrationAdditionalFieldTextTime

@Composable
internal fun RenderAdditionalFields(
    modifier: Modifier = Modifier,
    fields: List<StartRegistrationStatementAnswer>,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (fields.isNotEmpty())
            OnBackgroundCard {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Дополнительные поля",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    fields.forEach { field ->
                        RenderAdditionalField(
                            field = field,
                            onFieldChanged = onFieldChanged
                        )
                    }
                }

            }
    }
}

@Composable
internal fun RenderAdditionalField(
    field: StartRegistrationStatementAnswer,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {

    when (field.field.type) {
        StartRegistrationAdditionalField.Type.TEXT -> StartRegistrationAdditionalFieldText(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.NUMBER -> AdditionalFieldNumber(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.CHECKBOX -> AdditionalFiledCheckBox(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.DATA -> StartRegistrationAdditionalFieldTextData(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.TIME -> StartRegistrationAdditionalFieldTextTime(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.MULTISELECT -> AdditionalFieldMultiselect(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.SINGLE_SELECT -> AdditionalFieldSingleSelect(
            field = field,
            onFieldChanged = onFieldChanged
        )
    }
}