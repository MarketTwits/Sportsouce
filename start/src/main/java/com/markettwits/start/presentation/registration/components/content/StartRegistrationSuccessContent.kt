package com.markettwits.start.presentation.registration.components.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.registration.components.DateProfileTextField
import com.markettwits.start.presentation.registration.components.FilterPosition
import com.markettwits.start.presentation.registration.components.RegistrationDialog
import com.markettwits.start.presentation.registration.components.RegistrationTextField
import com.markettwits.start.presentation.registration.components.StartRegistrationButtonPanel
import com.markettwits.start.presentation.registration.components.fileds.CityFiled
import com.markettwits.start.presentation.registration.components.fileds.RegistrationFiled
import com.markettwits.start.presentation.registration.components.fileds.TeamFiled
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartRegistrationSuccessContent(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit,
    onClickSave : OnClick, onClickPay : OnClick
) {

    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        RegistrationTextField(
            label = "Имя",
            value = statement.name,
            onValueChange = {
                onValueChanged(statement.copy(name = it))
            }
        )
        RegistrationTextField(
            label = "Фамилия",
            value = statement.surname,
            onValueChange = {
                onValueChanged(statement.copy(surname = it))
            }
        )
        val calendarState = rememberUseCaseState()
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                locale = getDefault(),
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date { newDate ->
                onValueChanged(statement.copy(birthday = newDate.mapToString()))
            },
        )
        RegistrationTextField(
            modifier = Modifier.clickable {
                calendarState.show()
            },
            enabled = false,
            label = "День рождения",
            value = statement.birthday,
            onValueChange = {
                onValueChanged(statement.copy(birthday = it))
            }
        )
        RegistrationTextField(
            label = "Почта",
            value = statement.email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {
                onValueChanged(statement.copy(email = it))
            }
        )
        RegistrationTextField(
            label = "Номер телефона",
            value = statement.phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = {
                onValueChanged(statement.copy(phone = it))
            }
        )
        RegistrationFiled(
            label = "Пол",
            value = statement.sex,
            items = statement.sexList.map { it.name },
            onValueChanged = {
                onValueChanged(statement.copy(sex = it))
            }
        )
        CityFiled(
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        TeamFiled(
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        RegistrationTextField(
            label = "Промокод",
            value = statement.promocode,
            onValueChange = {
                onValueChanged(statement.copy(promocode = it))
            }
        )
        StartRegistrationButtonPanel(
            onClickSave = {
                onClickSave()
            }, onClickPay = {
                onClickPay()
            })
    }
}