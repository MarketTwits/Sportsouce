package com.markettwits.profile.presentation.component.edit_profile.presentation.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.common.text_filed.DateProfileTextField
import com.markettwits.profile.presentation.common.text_filed.MyProfileTextField
import com.markettwits.profile.presentation.component.edit_profile.models.CityUi
import com.markettwits.profile.presentation.component.edit_profile.models.TeamUi
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale.filter
import java.util.Locale.getDefault


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataPage(
    page: EditProfileUiPage.UserData,
    onUserChange: (EditProfileUiPage.UserData) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
    ) {
        val modifier = Modifier.padding(5.dp)
        MyProfileTextField(
            modifier = modifier,
            value = page.name,
            onValueChange = { newValue -> onUserChange(page.copy(name = newValue)) },
            label = "Имя"
        )
        MyProfileTextField(
            modifier = modifier,
            value = page.surname,
            onValueChange = { newValue -> onUserChange(page.copy(surname = newValue)) },
            label = "Фамилия"
        )
        MyProfileTextField(
            modifier = modifier,
            value = page.sex,
            onValueChange = { newValue -> onUserChange(page.copy(sex = newValue)) },
            label = "Пол"
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
                onUserChange(page.copy(birthday = newDate.mapToString()))
            },
        )
        DateProfileTextField(
            modifier = modifier,
            value = page.birthday,
            onValueChange = { newValue -> onUserChange(page.copy(birthday = newValue)) },
            label = "День рождения"
        ) {
            calendarState.show()
        }

        MyProfileTextField(
            modifier = modifier,
            value = page.phoneNumber,
            onValueChange = { newValue -> onUserChange(page.copy(phoneNumber = newValue)) },
            label = "Номер телефона"
        )
        AutoComplete(
            categories = page._cities.map { it.name },
            value = page.city, label = "Город",
            onValueChange = { newValue -> onUserChange(page.copy(city = newValue)) })

        AutoComplete(
            categories = page._teams.map { it.name },
            value = page.team, label = "Команда",
            onValueChange = { newValue -> onUserChange(page.copy(team = newValue)) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDatePicker() {
    val calendarState = rememberUseCaseState()
    CalendarDialog(state = calendarState, selection = CalendarSelection.Date {

    })
}
