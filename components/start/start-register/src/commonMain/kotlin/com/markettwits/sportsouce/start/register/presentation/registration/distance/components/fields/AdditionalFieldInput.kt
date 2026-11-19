package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField

@Composable
internal fun AdditionalFieldInput(
    field: StartRegistrationAdditionalField,
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: ((String) -> Unit)? = null,
) {
    val description = field.description?.takeIf { it.isNotBlank() }
    SauceTextField(
        label = field.labelWithPrice() + if (field.isOptional) "" else " *",
        value = value,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        readOnly = onValueChange == null,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange ?: {},
        supportingContent = description?.let { hint ->
            {
                Text(
                    text = hint,
                    modifier = Modifier.padding(top = SauceSpacing.extraSmall),
                    style = MaterialTheme.typography.bodySmall,
                    color = SauceTheme.colors.textMuted,
                )
            }
        },
    )
}
