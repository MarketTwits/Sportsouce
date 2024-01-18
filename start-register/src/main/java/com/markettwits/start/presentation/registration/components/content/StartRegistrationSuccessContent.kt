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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.markettwits.start.domain.StartStatement
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
import java.time.LocalDate
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartRegistrationSuccessContent(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit,
    onClickSave: () -> Unit, onClickPay: () -> Unit,
    onPromoChanged: (String) -> Unit,
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
        val DEFAULT_RANGE_START_DATE = LocalDate.of(1920, 3, 15)
        val DEFAULT_RANGE_END_YEAR_OFFSET = 20L
        val DEFAULT_RANGE_END_DATE = LocalDate.now().plusYears(DEFAULT_RANGE_END_YEAR_OFFSET)
            .withMonth(1)
            .withDayOfMonth(15)
        val DEFAULT_RANGE = DEFAULT_RANGE_START_DATE..DEFAULT_RANGE_END_DATE
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                locale = getDefault(),
                boundary = DEFAULT_RANGE,
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
                onPromoChanged(it)
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