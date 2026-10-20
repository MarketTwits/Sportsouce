package com.markettwits.sportsouce.edit_profile.info.presentation.components

import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.edit_profile.change_password.presentation.component.SaveChangesButton
import com.markettwits.sportsouce.edit_profile.info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore


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
    var feedbackSuccess by remember { mutableStateOf(true) }
    val feedbackColors = if (feedbackSuccess) SauceTheme.colors.success else SauceTheme.colors.danger

    // Constants for better maintainability
    val fieldPadding = 8.dp
    val sexOptions = listOf("Мужской", "Женский")

    Scaffold(
        modifier = modifier,
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
                    contentColor = feedbackColors.content,
                    containerColor = feedbackColors.container,
                    dismissActionContentColor = feedbackColors.content,
                    snackbarData = it
                )
            }
        },
        bottomBar = {
            if (state.userData == null || state.error != null) return@Scaffold
            AdaptivePane(fillHeight = false) { SaveChangesButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding().padding(horizontal = 16.dp, vertical = 12.dp),
                loading = state.isLoading,
                enabled = !state.isLoading,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onClickSave()
                }
            ) }
        }
    ) { paddingValues ->
        AdaptivePane {
            state.userData?.let { user ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues).consumeWindowInsets(paddingValues)
                        .verticalScroll(rememberScrollState()).padding(16.dp)
                ) {
                    OnBackgroundCard(
                        modifier = Modifier.padding(top = 4.dp),
                    ) {

                        Column(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Личные данные", Modifier.padding(8.dp), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                            // Name field with Next action
                            OutlinedTextFieldBase(
                                modifier = Modifier
                                    .padding(fieldPadding)
                                    .focusRequester(nameFocusRequester),
                                value = user.name,
                                onValueChange = { newValue -> onUserChange(user.copy(name = newValue)) },
                                label = "Имя",
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.Person)
                                },
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
                                modifier = Modifier
                                    .padding(fieldPadding)
                                    .focusRequester(surnameFocusRequester),
                                value = user.surname,
                                onValueChange = { newValue -> onUserChange(user.copy(surname = newValue)) },
                                label = "Фамилия",
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.Person)
                                },
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
                                modifier = Modifier
                                    .padding(fieldPadding),
                                textFiled = { textFieldModifier ->
                                    OutlinedTextFieldBase(
                                        modifier = textFieldModifier,
                                        label = "День рождения",
                                        value = user.birthday,
                                        isEnabled = false,
                                        leadingIcon = {
                                            FieldLeadingIcon(Icons.Default.CalendarMonth)
                                        },
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
                                modifier = Modifier
                                    .padding(fieldPadding)
                                    .focusRequester(phoneNumberFocusRequester),
                                value = user.phoneNumber,
                                onValueChange = { newValue -> onUserChange(user.copy(phoneNumber = newValue)) },
                                label = "Номер телефона",
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.Phone)
                                },
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
                                modifier = Modifier
                                    .padding(fieldPadding),
                                itemList = sexOptions,
                                selectedItem = user.sex,
                                onItemSelected = { _, selectedItem ->
                                    onUserChange(user.copy(sex = selectedItem))
                                },
                                textFiled = {
                                    OutlinedTextFieldBase(
                                        label = "Пол",
                                        value = user.sex,
                                        isEnabled = false,
                                        leadingIcon = {
                                            FieldLeadingIcon(Icons.Default.Person)
                                        }
                                    ) {}
                                }
                            )

                            Text("Участие", Modifier.padding(8.dp), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                            // City selection dialog
                            ItemsTextFiledDialog(
                                modifier = Modifier
                                    .padding(fieldPadding),
                                label = "Город",
                                value = user.city,
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.LocationOn)
                                },
                                items = state.cities
                                    .sortedBy { it.name }
                                    .map { it.name },
                                onValueChanged = { newValue ->
                                    onUserChange(user.copy(city = newValue))
                                }
                            )

                            // Team selection dialog
                            ItemsTextFiledDialog(
                                modifier = Modifier
                                    .padding(fieldPadding),
                                label = "Команда",
                                value = user.team,
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.Groups)
                                },
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
            }
            if (state.isLoading && state.userData == null && state.error == null) {
                LoadingFullScreen(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
            }
            state.error?.SauceErrorScreen(
                modifier = Modifier.fillMaxSize(),
                onClickRetry = onClickRetry
            )
            EventEffect(
                event = state.event,
                onConsumed = onConsume,
            ) {
                feedbackSuccess = it.success
                snackBarHostState.showLongMessageWithDismiss(message = it.message)
            }
        }
    }
}

@Composable
private fun FieldLeadingIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = SauceTheme.colors.textSecondary
    )
}
