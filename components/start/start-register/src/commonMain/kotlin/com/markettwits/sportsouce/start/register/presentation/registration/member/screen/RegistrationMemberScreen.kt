package com.markettwits.sportsouce.start.register.presentation.registration.member.screen

import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import com.markettwits.core_ui.items.screens.AdaptivePane
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.register.presentation.registration.member.component.RegistrationMemberComponent
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.MemberContinueButton
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.MemberScreenContent
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.isMemberFormValid
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isPresentInProfileMembers
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidEmail
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidPhone
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore

@Composable
fun RegistrationMemberScreen(
    modifier: Modifier = Modifier,
    component: RegistrationMemberComponent,
) {
    val state by component.model.collectAsState()
    val isFormValid = isMemberFormValid(
        statement = state.value,
        contactPerson = state.value.contactPerson
    )
    val isInProfile = state.value.isPresentInProfileMembers(state.members)
    val isAddToProfileEnabled =
        isFormValid && !isInProfile && !state.isAddMemberLoading
    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdaptivePane(fillHeight = false) {
                MemberContinueButton(
                    modifier = Modifier.navigationBarsPadding().imePadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    enabled = isFormValid && state.isSelectedTeamAvailable,
                    onClickContinue = { component.obtainEvent(RegistrationMemberStore.Intent.OnClickContinue) },
                    onClickDisabled = { component.obtainEvent(RegistrationMemberStore.Intent.OnClickContinue) },
                )
            }
        },
        topBar = {
            TopBarWithClip(title = "Регистрация") {
                component.obtainEvent(RegistrationMemberStore.Intent.Pop)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        }
    ) {
        MemberScreenContent(
            modifier = Modifier.padding(it).consumeWindowInsets(it),
            userNumber = state.userNumber,
            statement = state.value,
            members = state.members,
            membersLoading = state.membersLoading,
            membersError = state.membersError ?: if (!state.membersLoading && !state.isSelectedTeamAvailable)
                "Участник команды больше недоступен. Обновите список или выберите другого участника." else null,
            onRetryMembers = { component.obtainEvent(RegistrationMemberStore.Intent.RetryMembers) },
            onMemberSelected = { component.obtainEvent(RegistrationMemberStore.Intent.SelectMember(it)) },
            onValueChanged = {
                component.obtainEvent(RegistrationMemberStore.Intent.ChangeFiled(it))
            },
            isAddToProfileEnabled = isAddToProfileEnabled,
            onClickAddToProfile = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnClickAddToProfile)
            },
        )
        EventEffect(
            event = state.event,
            onConsumed = {
                component.obtainEvent(RegistrationMemberStore.Intent.OnConsumedEvent)
            },
        ) {
            snackBarColor =
                if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
            snackBarHostState.showLongMessageWithDismiss(message = it.message)
        }
        if (state.isSuggestAddDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnDismissSuggestAddDialog)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                title = {
                    Text(
                        text = "Добавить участника в профиль?",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold()
                    )
                },
                text = {
                    Text(
                        text = "Этот участник ещё не сохранён в вашем профиле. Добавить его перед продолжением?",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.medium()
                    )
                },
                confirmButton = {
                    SauceButton("Добавить", fillWidth = false,
                        onClick = { component.obtainEvent(RegistrationMemberStore.Intent.OnConfirmSuggestAddDialog) })
                },
                dismissButton = {
                    SauceButton("Продолжить без добавления", fillWidth = false,
                        variant = SauceButtonVariant.Text,
                        onClick = { component.obtainEvent(RegistrationMemberStore.Intent.OnContinueWithoutAdd) })
                },
            )
        }
        if (state.isAddMemberDialogVisible) {
            val shouldRequestContacts =
                !state.addMemberEmail.trim().isValidEmail() || !state.addMemberPhone.trim().isValidPhone()
            AddMemberToProfileDialog(
                relationType = state.addMemberRelationType,
                email = state.addMemberEmail,
                phone = state.addMemberPhone,
                shouldRequestContacts = shouldRequestContacts,
                showValidation = state.isAddMemberValidationVisible,
                dialogErrorMessage = state.addMemberDialogErrorMessage,
                isLoading = state.isAddMemberLoading,
                onDismiss = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnDismissAddMemberDialog)
                },
                onRelationTypeChanged = {
                    component.obtainEvent(
                        RegistrationMemberStore.Intent.OnChangeAddMemberRelationType(it)
                    )
                },
                onEmailChanged = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnChangeAddMemberEmail(it))
                },
                onPhoneChanged = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnChangeAddMemberPhone(it))
                },
                onConfirm = {
                    component.obtainEvent(RegistrationMemberStore.Intent.OnConfirmAddMemberToProfile)
                }
            )
        }
    }
}
