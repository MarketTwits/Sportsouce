package com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.edit_profile.edit_profile_change_password.presentation.component.SaveChangesButton
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore


@Composable
fun EditProfileInfoFieldsContent(
    modifier: Modifier = Modifier,
    state: EditProfileInfoStore.State,
    onUserChange: (UserData) -> Unit,
    onConsume: () -> Unit,
    onClickRetry: () -> Unit,
    onClickGoBack: () -> Unit,
    onClickSave: () -> Unit,
) {
    // Keyboard and focus management
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Focus requesters for navigation between fields
    val nameFocusRequester = remember { FocusRequester() }
    val surnameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }

    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarColor = remember {
        SportSouceColor.SportSouceLighBlue
    }

    // Constants for better maintainability
    val fieldPadding = 8.dp
    val cardPadding = 12.dp
    val sexOptions = listOf("Мужской", "Женский")

    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBarWithClip(
                title = "Редактировать профиль",
                goBack = onClickGoBack,
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    dismissActionContentColor = Color.White,
                    snackbarData = it
                )
            }
        },
        floatingActionButton = {
            if (state.isLoading) return@Scaffold
            SaveChangesButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .padding(bottom = 12.dp),
                loading = state.isLoading,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onClickSave()
                }
            )
        }
    ) { paddingValues ->

        OnBackgroundCard(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(12.dp)
        ) {
            state.userData?.let { user ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(cardPadding),
                    verticalArrangement = Arrangement.spacedBy(fieldPadding)
                ) {
                    // Name field with Next action
                    OutlinedTextFieldBase(
                        modifier = Modifier.focusRequester(nameFocusRequester),
                        value = user.name,
                        onValueChange = { newValue -> onUserChange(user.copy(name = newValue)) },
                        label = "Имя",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                surnameFocusRequester.requestFocus()
                            }
                        )
                    )

                    // Surname field with Next action
                    OutlinedTextFieldBase(
                        modifier = Modifier.focusRequester(surnameFocusRequester),
                        value = user.surname,
                        onValueChange = { newValue -> onUserChange(user.copy(surname = newValue)) },
                        label = "Фамилия",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )

                    // Birthday field (calendar picker)
                    CalendarTextFiled(
                        modifier = Modifier,
                        textFiled = { textFieldModifier ->
                            OutlinedTextFieldBase(
                                modifier = textFieldModifier,
                                label = "День рождения",
                                value = user.birthday,
                                isEnabled = false,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        phoneNumberFocusRequester.requestFocus()
                                    }
                                )
                            ) {}
                        },
                        onValueChanged = { newValue ->
                            onUserChange(user.copy(birthday = newValue))
                        }
                    )

                    // Phone number field with Done action (last editable field)
                    OutlinePhoneTextFiled(
                        modifier = Modifier.focusRequester(phoneNumberFocusRequester),
                        value = user.phoneNumber,
                        onValueChange = { newValue -> onUserChange(user.copy(phoneNumber = newValue)) },
                        label = "Номер телефона",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        )
                    )

                    // Gender selection dropdown
                    DropDownSpinner(
                        modifier = Modifier,
                        itemList = sexOptions,
                        selectedItem = user.sex,
                        onItemSelected = { _, selectedItem ->
                            onUserChange(user.copy(sex = selectedItem))
                        },
                        textFiled = {
                            OutlinedTextFieldBase(
                                label = "Пол",
                                value = user.sex,
                                isEnabled = false
                            ) {}
                        }
                    )

                    // City selection dialog
                    ItemsTextFiledDialog(
                        modifier = Modifier,
                        label = "Город",
                        value = user.city,
                        items = state.cities
                            .sortedBy { it.name }
                            .map { it.name },
                        onValueChanged = { newValue ->
                            onUserChange(user.copy(city = newValue))
                        }
                    )

                    // Team selection dialog
                    ItemsTextFiledDialog(
                        modifier = Modifier,
                        label = "Команда",
                        value = user.team,
                        items = state.teams
                            .sortedBy { it.name }
                            .map { it.name },
                        onValueChanged = { newValue ->
                            onUserChange(user.copy(team = newValue))
                        }
                    )
                }
            }
        }
        if (state.isLoading) {
            LoadingFullScreen(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickBack = onClickGoBack,
                onClickRetry = onClickRetry
            )
        }
        EventEffect(
            event = state.event,
            onConsumed = onConsume,
        ) {
            snackBarColor =
                if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
    }
}
