package com.markettwits.sportsouce.start.register.presentation.registration.member.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.RegistrationMemberValidator
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isPresentInProfileMembers
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidEmail
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidPhone
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class RegistrationMemberExecutor(
    private val validation: RegistrationMemberValidator,
    private val profileMembersRepository: ProfileMembersRepository,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeAction(action: Unit) {
        scope.launch {
            runCatching {
                profileMembersRepository.fetchMembers(withUser = true)
            }.onSuccess {
                dispatch(Message.OnMembersChanged(it))
            }
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeFiled -> dispatch(Message.OnValueChanged(intent.startStatement.updateAge()))
            is Intent.OnClickContinue -> onClickContinue(state())
            is Intent.OnClickAddToProfile -> onClickAddToProfile(state())
            is Intent.OnDismissSuggestAddDialog -> dispatch(Message.ChangeSuggestAddDialogState(false))
            is Intent.OnConfirmSuggestAddDialog -> {
                validation.validateFields(state().value.copy(contactPerson = false)).fold(
                    onSuccess = {
                        dispatch(Message.ChangeSuggestAddDialogState(false))
                        dispatch(Message.ChangePendingContinueAfterAdd(true))
                        dispatch(Message.ChangeAddMemberEmail(state().value.email))
                        dispatch(Message.ChangeAddMemberPhone(state().value.phone))
                        dispatch(Message.ChangeAddMemberValidationVisible(false))
                        dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
                        dispatch(Message.ChangeAddMemberDialogState(true))
                    },
                    onFailure = {
                        dispatch(Message.ShowEvent(it.message.toString()))
                    }
                )
            }

            is Intent.OnContinueWithoutAdd -> {
                dispatch(Message.ChangeSuggestAddDialogState(false))
                publish(Label.OnClickContinue(state().value))
            }

            is Intent.OnDismissAddMemberDialog -> {
                dispatch(Message.ChangePendingContinueAfterAdd(false))
                dispatch(Message.ChangeAddMemberValidationVisible(false))
                dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
                dispatch(Message.ChangeAddMemberDialogState(false))
            }

            is Intent.OnChangeAddMemberRelationType -> dispatch(
                Message.ChangeAddMemberRelationType(intent.value)
            )

            is Intent.OnChangeAddMemberEmail -> {
                dispatch(Message.ChangeAddMemberEmail(intent.value))
                dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
            }

            is Intent.OnChangeAddMemberPhone -> {
                dispatch(Message.ChangeAddMemberPhone(intent.value))
                dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
            }

            is Intent.OnConfirmAddMemberToProfile -> addMemberToProfile(state())
            is Intent.Pop -> publish(Label.OnClickPop)
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
        }
    }

    private fun onClickContinue(state: State) {
        val startStatement = state.value
        validation.validateFields(startStatement).fold(
            onSuccess = {
                if (startStatement.isPresentInProfileMembers(state.members)) {
                    publish(Label.OnClickContinue(startStatement))
                } else {
                    dispatch(Message.ChangeSuggestAddDialogState(true))
                }
            }, onFailure = {
                dispatch(Message.ShowEvent(it.message.toString()))
            })
    }

    private fun onClickAddToProfile(state: State) {
        validation.validateFields(state.value.copy(contactPerson = false)).fold(
            onSuccess = {
                if (state.value.isPresentInProfileMembers(state.members)) {
                    dispatch(Message.ShowEvent("Участник уже есть в профиле"))
                } else {
                    dispatch(Message.ChangePendingContinueAfterAdd(false))
                    dispatch(Message.ChangeAddMemberEmail(state.value.email))
                    dispatch(Message.ChangeAddMemberPhone(state.value.phone))
                    dispatch(Message.ChangeAddMemberValidationVisible(false))
                    dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
                    dispatch(Message.ChangeAddMemberDialogState(true))
                }
            },
            onFailure = {
                dispatch(Message.ShowEvent(it.message.toString()))
            }
        )
    }

    private fun addMemberToProfile(state: State) {
        if (state.isAddMemberLoading) return
        dispatch(Message.ChangeAddMemberValidationVisible(true))
        dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
        if (!isAddMemberContactsValid(state.addMemberEmail, state.addMemberPhone)) {
            return
        }
        if (state.value.isPresentInProfileMembers(
                members = state.members,
                emailOverride = state.addMemberEmail,
                phoneOverride = state.addMemberPhone
            )
        ) {
            dispatch(Message.ChangeAddMemberDialogErrorMessage("Участник с такими данными уже есть в профиле"))
            return
        }

        scope.launch {
            dispatch(Message.ChangeAddMemberLoading(true))
            val member = state.value.toProfileMember(
                type = state.addMemberRelationType,
                email = state.addMemberEmail.trim(),
                phone = state.addMemberPhone.trim()
            )

            profileMembersRepository.addMember(member).fold(
                onSuccess = {
                    runCatching {
                        profileMembersRepository.fetchMembers(withUser = true)
                    }.onSuccess {
                        dispatch(Message.OnMembersChanged(it))
                    }
                    dispatch(Message.ChangeAddMemberDialogState(false))
                    dispatch(Message.ChangeAddMemberLoading(false))
                    dispatch(Message.ChangeAddMemberValidationVisible(false))
                    dispatch(Message.ChangeAddMemberDialogErrorMessage(null))
                    dispatch(Message.ShowEvent("Участник добавлен в профиль", success = true))
                    dispatch(Message.ChangePendingContinueAfterAdd(false))
                },
                onFailure = {
                    dispatch(Message.ChangeAddMemberLoading(false))
                    dispatch(
                        Message.ChangeAddMemberDialogErrorMessage(
                            it.mapToSauceError().mapToString() ?: "Не удалось добавить участника"
                        )
                    )
                }
            )
        }
    }

    private fun StartStatement.toProfileMember(
        type: String,
        email: String,
        phone: String,
    ): ProfileMember {
        val age = age.toIntOrNull() ?: 0
        val isChild = age <= 18
        return ProfileMember(
            id = 0,
            userId = 0,
            name = name,
            surname = surname,
            email = email,
            phone = phone,
            gender = sex,
            team = team,
            birthday = birthday,
            type = type,
            child = isChild
        )
    }

    private fun isAddMemberContactsValid(email: String, phone: String): Boolean {
        val normalizedEmail = email.trim()
        val normalizedPhone = phone.trim()
        return normalizedEmail.isValidEmail() && normalizedPhone.isValidPhone()
    }

    @OptIn(ExperimentalTime::class)
    private fun StartStatement.updateAge(): StartStatement {
        return if (birthday.isNotEmpty()) {
            val birthLocalDate = try {
                val parts = birthday.split(".")
                LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt()) // LocalDate(year, month, day)
            } catch (_: Exception) {
                return copy()
            }

            val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val years = currentDate.year - birthLocalDate.year - if (
                currentDate.month.number < birthLocalDate.month.number ||
                (currentDate.month.number == birthLocalDate.month.number && currentDate.day < birthLocalDate.day)
            ) 1 else 0

            copy(age = max(0, years).toString())
        } else {
            copy()
        }
    }
}
