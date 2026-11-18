package com.markettwits.sportsouce.start.register.presentation.registration.distance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.visibleAdditionalFieldAnswers
import com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields.*

@Composable
internal fun RenderAdditionalFields(
    modifier: Modifier = Modifier,
    fields: List<StartRegistrationStatementAnswer>,
    onFieldChanged: (StartRegistrationStatementAnswer) -> Unit
) {
    if (fields.isEmpty()) return

    OnBackgroundCard(modifier = modifier) { contentModifier ->
        Column(
            modifier = contentModifier.padding(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.large),
        ) {
            Text(
                text = "Дополнительные поля",
                style = MaterialTheme.typography.titleSmall,
                color = SauceTheme.colors.textPrimary,
            )
            fields.visibleAdditionalFieldAnswers().forEach { field ->
                key(field.field.id, field.field.type) {
                    RenderAdditionalField(field = field, onFieldChanged = onFieldChanged)
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
        StartRegistrationAdditionalField.Type.TEXT -> AdditionalFieldText(
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

        StartRegistrationAdditionalField.Type.DATA -> AdditionalFieldTextData(
            field = field,
            onFieldChanged = onFieldChanged
        )

        StartRegistrationAdditionalField.Type.TIME -> AdditionalFieldTextTime(
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
