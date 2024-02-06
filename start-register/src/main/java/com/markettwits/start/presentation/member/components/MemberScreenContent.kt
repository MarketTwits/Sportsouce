package com.markettwits.start.presentation.member.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.components.RegistrationTextField
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
fun MemberScreenContent(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit,
    onClickContinue: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
//        var contactFace by remember {
//            mutableStateOf(statement.contactPerson)
//        }
        MemberContactFace(checked = statement.contactPerson, onValueChanged = {
            onValueChanged(statement.copy(contactPerson = it))
        }
        )
        Text(
            text = "3 этап",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        RegistrationTextField(
            modifier = Modifier.padding(vertical = 5.dp),
            label = "Имя",
            value = statement.name,
            onValueChange = {
                onValueChanged(statement.copy(name = it))
            }
        )
        RegistrationTextField(
            modifier = Modifier.padding(vertical = 5.dp),
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
            modifier = Modifier
                .padding(vertical = 5.dp)
                .clickable {
                    calendarState.show()
                },
            enabled = false,
            label = "День рождения",
            value = statement.birthday,
            onValueChange = {
                onValueChanged(statement.copy(birthday = it))
            }
        )
        if (statement.contactPerson) {
            RegistrationTextField(
                modifier = Modifier.padding(vertical = 5.dp),
                label = "Почта",
                value = statement.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    onValueChanged(statement.copy(email = it))
                }
            )
            RegistrationTextField(
                modifier = Modifier.padding(vertical = 5.dp),
                label = "Номер телефона",
                value = statement.phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                onValueChange = {
                    onValueChanged(statement.copy(phone = it))
                }
            )
        }
        RegistrationFiled(
            modifier = Modifier.padding(vertical = 5.dp),
            label = "Пол",
            value = statement.sex,
            items = statement.sexList.map { it.name },
            onValueChanged = {
                onValueChanged(statement.copy(sex = it))
            }
        )
        CityFiled(
            modifier = Modifier.padding(vertical = 5.dp),
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        TeamFiled(
            modifier = Modifier.padding(vertical = 5.dp),
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        MemberContinueButton(onClickContinue = {
            onClickContinue()
        })
    }
}