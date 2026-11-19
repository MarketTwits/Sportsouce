package com.markettwits.sportsouce.start.support.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCase
import com.markettwits.sportsouce.start.support.domain.SupportAuthorizationRequired
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

internal class StartSupportExecutor(
    private val intentAction: IntentAction,
    private val useCase: StartSupportUseCase,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var checkJob: Job? = null
    private var paymentJob: Job? = null
    private var registrationAccountId: Int? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnChangeValue -> if (!state().isLoading) dispatch(Message.OnValueChanged(intent.value))
            Intent.OnClickSupport -> donate()
            Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.ApplyStartId -> {
                val sameStart = intent.startId == state().startId
                if (!sameStart) {
                    checkJob?.cancel()
                    paymentJob?.cancel()
                    dispatch(Message.UpdateStartId(intent.startId))
                }
                checkRegistration(refreshRegistrations = sameStart)
            }
            Intent.OnOpen -> {
                if (!state().isOpen) open()
            }
            Intent.OnClose -> {
                paymentJob?.cancel()
                dispatch(Message.Close)
            }
            Intent.OnRetryCheck -> if (state().isOpen && !state().isLoading) checkRegistration()
            Intent.OnConfirmDonation -> if (state().checkState is CheckState.Ready && !state().isLoading) {
                dispatch(Message.ConfirmDonation)
            }
            Intent.OnPayRegistration -> payRegistration()
            Intent.OnSignIn -> if (state().isOpen && state().checkState == CheckState.AuthorizationRequired) {
                dispatch(Message.SuspendForAuth)
                publish(Label.SignIn)
            }
            Intent.OnResume -> {
                if (state().awaitingAuth) {
                    open()
                } else if (state().startId != null && !state().isLoading) {
                    checkJob?.cancel()
                    checkRegistration(refreshRegistrations = true)
                }
            }
        }
    }

    private fun open() {
        dispatch(Message.Open)
        checkRegistration()
    }

    private fun checkRegistration(refreshRegistrations: Boolean = false) {
        val startId = state().startId ?: return
        if (checkJob?.isActive == true || state().isLoading || state().awaitingAuth) return
        if (state().isOpen) dispatch(Message.CheckLoading)
        checkJob = scope.launch {
            val cached = useCase.cachedRegistration(startId)
            coroutineContext.ensureActive()
            val snapshot = cached.getOrNull()
            if (snapshot != null) {
                if (registrationAccountId != snapshot.accountId || state().registrationCheck == null) {
                    dispatch(Message.ShowCachedRegistration(snapshot.check))
                }
                registrationAccountId = snapshot.accountId
            } else if (cached.exceptionOrNull() is SupportAuthorizationRequired) {
                registrationAccountId = null
                dispatch(Message.AuthorizationRequired)
                return@launch
            }
            val result = useCase.checkDonation(startId, refreshRegistrations)
            coroutineContext.ensureActive()
            if (state().startId != startId) return@launch
            result.fold(
                onSuccess = { dispatch(Message.CheckReady(it)) },
                onFailure = {
                    if (it is CancellationException) throw it
                    dispatch(if (it is SupportAuthorizationRequired) Message.AuthorizationRequired else Message.CheckFailed)
                },
            )
        }
    }

    private fun donate() {
        val current = state()
        val check = (current.checkState as? CheckState.Ready)?.result ?: return
        if (!current.isOpen || current.isLoading || (check.requiresConfirmation && !current.donationConfirmed)) return
        val startId = current.startId ?: return
        val validAmount = useCase.validate(current.cost)
        if (validAmount.isFailure) {
            dispatch(Message.InvalidAmount(validAmount.exceptionOrNull()?.message ?: "Проверьте сумму пожертвования"))
            return
        }
        launchPayment { useCase.donation(startId, current.cost) }
    }

    private fun payRegistration() {
        val current = state()
        val check = (current.checkState as? CheckState.Ready)?.result ?: return
        val registration = check.registration ?: return
        if (!current.isOpen || current.isLoading || !check.hasUnpaidRegistration ||
            !registration.canPay || registration.groupId <= 0) return
        launchPayment { useCase.payRegistration(registration.groupId) }
    }

    private fun launchPayment(request: suspend () -> Result<String>) {
        dispatch(Message.Loading)
        paymentJob = scope.launch {
            val result = request()
            coroutineContext.ensureActive()
            if (!state().isOpen) return@launch
            result.fold(
                onSuccess = { url ->
                    if (url.isBlank()) {
                        dispatch(Message.Failure("Ссылка на оплату недоступна. Попробуйте ещё раз."))
                    } else {
                        intentAction.openWebPage(url)
                        dispatch(Message.Success(url))
                    }
                },
                onFailure = {
                    if (it is CancellationException) throw it
                    dispatch(if (it is SupportAuthorizationRequired) Message.AuthorizationRequired
                        else Message.Failure("Не удалось перейти к оплате. Попробуйте ещё раз."))
                },
            )
        }
    }
}
