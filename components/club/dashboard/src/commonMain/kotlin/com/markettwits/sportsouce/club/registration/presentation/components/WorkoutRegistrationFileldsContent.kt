package com.markettwits.sportsouce.club.registration.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm

@Composable
internal fun WorkoutRegistrationFieldsContent(
    modifier: Modifier = Modifier,
    form: WorkoutRegistrationForm,
    onValueChanged: (WorkoutRegistrationForm) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextFieldBase(
            modifier = Modifier.padding(4.dp),
            label = "Фамилия",
            value = form.surname,
            onValueChange = {
                onValueChanged(form.copy(surname = it))
            },
        )
        OutlinedTextFieldBase(
            modifier = Modifier.padding(4.dp),
            label = "Имя",
            value = form.name,
            onValueChange = {
                onValueChanged(form.copy(name = it))
            },
        )
        OutlinePhoneTextFiled(
            modifier = Modifier.padding(4.dp),
            label = "Номер телефона",
            value = form.phone,
            onValueChange = {
                onValueChanged(form.copy(phone = it))
            },
        )
    }
}