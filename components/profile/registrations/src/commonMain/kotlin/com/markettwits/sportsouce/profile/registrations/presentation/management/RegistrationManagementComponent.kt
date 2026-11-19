package com.markettwits.sportsouce.profile.registrations.presentation.management

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.SportSauceRegistrationManagementApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.*
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.*

class RegistrationManagementComponent internal constructor(
    context: ComponentContext,
    dependencies: Dependencies,
    onRegistrationChanged: () -> Unit,
) {
    internal class Dependencies(
        val api: SportSauceRegistrationManagementApi,
        val profile: SportSauceNetworkProfileApi,
        val auth: AuthDataSource,
        val actions: IntentAction,
    )

    internal sealed interface Sheet {
        val title: String

        data object Find : Sheet { override val title = "Поиск регистраций" }
        data class External(val entry: ExternalRegistration? = null) : Sheet {
            override val title = if (entry == null) "Внешняя регистрация" else "Редактировать регистрацию"
        }
        data class Delete(val entry: ExternalRegistration) : Sheet { override val title = "Удалить регистрацию?" }
        data class Correct(val member: StartOrderMember) : Sheet { override val title = "Исправить опечатку" }
        data class Transfer(val member: StartOrderMember) : Sheet { override val title = "Передать слот" }
        data class Pay(val order: StartOrderInfo) : Sheet { override val title = "Оплата регистрации" }
        data class TransferStatus(val invitation: OutgoingRegistrationTransfer) : Sheet { override val title = "Передача слота" }
        data class Refund(val order: StartOrderInfo) : Sheet { override val title = "Вернуть слот" }
        data class Options(val order: StartOrderInfo) : Sheet { override val title = "Дополнительные опции" }
    }

    internal data class State(
        val sheet: Sheet? = null,
        val user: SharedUser? = null,
        val refundedOrderIds: Set<Int> = emptySet(),
        val transfers: List<OutgoingRegistrationTransfer> = emptyList(),
        val transfersLoading: Boolean = false,
        val transfersError: String? = null,
        val walletBalance: Int? = null,
        val walletError: String? = null,
        val paymentLink: String? = null,
        val externalRetryMore: Boolean = false,
        val price: RegistrationPriceQuote? = null,
        val appliedPromo: String = "",
        val optionPreviews: Map<Int, RegistrationOptions> = emptyMap(),
        val optionPreviewErrors: Map<Int, String> = emptyMap(),
        val previewGeneration: Int = 0,
        val candidates: List<RegistrationCandidate>? = null,
        val external: List<ExternalRegistration>? = null,
        val externalHasMore: Boolean = false,
        val externalNextOffset: Int = 0,
        val externalLoading: Boolean = false,
        val externalRefreshing: Boolean = false,
        val externalError: String? = null,
        val refundRules: RegistrationRefundRules? = null,
        val transferOptions: RegistrationTransferOptions? = null,
        val recipient: RegistrationTransferRecipient? = null,
        val options: RegistrationOptions? = null,
        val loading: Boolean = false,
        val submitting: Boolean = false,
        val error: String? = null,
        val notice: String? = null,
    )

    internal sealed interface Intent {
        data class Open(val sheet: Sheet) : Intent
        data object Close : Intent
        data object Retry : Intent
        data object LoadParticipantContext : Intent
        data class ApplyPromo(val promo: String) : Intent
        data class PreviewOptions(val orderId: Int) : Intent
        data object ReloadPreviews : Intent
        data object ClearNotice : Intent
        data object ReopenPayment : Intent
        data object RetryWallet : Intent
        data class ExternalList(val more: Boolean = false, val refresh: Boolean = false) : Intent
        data class OpenUrl(val url: String) : Intent
        data class FindRecipient(val query: String) : Intent
        data class Submit(val command: Command) : Intent
    }

    internal sealed interface Command {
        data class Attach(val ids: Set<Int>) : Command
        data class SaveExternal(val draft: ExternalRegistrationDraft) : Command
        data class DeleteExternal(val id: Int) : Command
        data class Correct(val correction: RegistrationCorrection) : Command
        data class Transfer(val memberId: Int, val transfer: RegistrationTransfer) : Command
        data class Refund(val orderId: Int, val groupId: Int, val percent: Int) : Command
        data class PayOptions(val orderId: Int, val selections: List<Pair<RegistrationOption, JsonElement?>>) : Command
        data class ChangeOption(val orderId: Int, val option: RegistrationOption, val value: JsonElement) : Command
        data class PayChange(val id: Int) : Command
        data class PayOrder(val orderId: Int, val promo: String) : Command
        data class RevokeTransfer(val id: Int) : Command
        data class PayTransfer(val id: Int, val method: String) : Command
    }

    private enum class Label { RegistrationChanged }

    private val store = context.instanceKeeper.getStore {
        DefaultStoreFactory().create<Intent, Unit, State, State, Label>(
            name = "RegistrationManagementStore",
            initialState = State(),
            executorFactory = { Executor(dependencies) },
            reducer = Reducer { state -> state },
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    internal val state = store.stateFlow

    internal fun accept(intent: Intent) = store.accept(intent)

    init {
        val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            store.labels.collect { onRegistrationChanged() }
        }
        context.lifecycle.doOnDestroy { scope.cancel() }
    }

    private class Executor(private val deps: Dependencies) : CoroutineExecutor<Intent, Unit, State, State, Label>() {
        private var sheetLoad: Job? = null
        private var externalLoad: Job? = null
        private var externalWindow = ExternalRegistrationWindow()
        private var participantsLoad: Job? = null
        private val previewJobs = mutableMapOf<Int, Job>()
        private val previewLimit = Semaphore(3)
        private val loadedPreviews = mutableSetOf<Int>()
        private var uploadedPoster: Pair<RegistrationPoster, Int>? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.Open -> if (!state().submitting) open(intent.sheet)
                Intent.Retry -> if (!state().submitting) state().sheet?.let(::open)
                Intent.LoadParticipantContext -> loadParticipants()
                is Intent.ApplyPromo -> applyPromo(intent.promo)
                is Intent.PreviewOptions -> loadPreview(intent.orderId)
                Intent.ReloadPreviews -> resetPreviews()
                Intent.Close -> if (!state().submitting) close()
                Intent.ReopenPayment -> {
                    val link = state().paymentLink ?: return
                    if (openPaymentLink(link)) {
                        close()
                        resetPreviews()
                        loadParticipants()
                        publish(Label.RegistrationChanged)
                    }
                }
                Intent.RetryWallet -> scope.launch { loadWalletBalance() }
                Intent.ClearNotice -> update { copy(notice = null) }
                is Intent.ExternalList -> loadExternal(intent.more, intent.refresh)
                is Intent.OpenUrl -> {
                    val opened = runCatching { deps.actions.tryOpenWebPage(intent.url) }.getOrDefault(false)
                    if (!opened) update {
                        if (sheet != null) copy(error = "Не удалось открыть ссылку.")
                        else copy(notice = "Не удалось открыть страницу старта.")
                    }
                }
                is Intent.FindRecipient -> findRecipient(intent.query)
                is Intent.Submit -> if (!state().submitting && !state().loading && state().paymentLink == null) submit(intent.command)
            }
        }

        private fun update(transform: State.() -> State) = dispatch(state().transform())
        private suspend fun token() = deps.auth.updateToken().getOrThrow()

        private fun close() {
            sheetLoad?.cancel()
            uploadedPoster = null
            update { copy(sheet = null, error = null, loading = false, recipient = null, paymentLink = null) }
        }

        private fun open(sheet: Sheet) {
            sheetLoad?.cancel()
            if (state().sheet != sheet) uploadedPoster = null
            val needsLoad = sheet is Sheet.Find || sheet is Sheet.Transfer || sheet is Sheet.Refund || sheet is Sheet.Options || sheet is Sheet.Pay
            update {
                copy(sheet = sheet, candidates = null, recipient = null, refundRules = null, transferOptions = null,
                    options = null, price = null, appliedPromo = "", loading = needsLoad, error = null, paymentLink = null)
            }
            if (!needsLoad) return
            sheetLoad = scope.launch {
                try {
                    val token = token()
                    when (sheet) {
                        Sheet.Find -> {
                            val candidates = deps.api.candidates(deps.auth.userID().getOrThrow(), token)
                            ensureActive()
                            update { copy(candidates = candidates) }
                        }
                        is Sheet.Transfer -> {
                            val options = deps.api.transferOptions(requireNotNull(sheet.member.id), token)
                            ensureActive()
                            update { copy(transferOptions = options) }
                        }
                        is Sheet.Pay -> {
                            val promo = initialRegistrationPromo(sheet.order)
                            val price = deps.api.price(sheet.order.id, promo, token)
                            ensureActive()
                            update { copy(price = price, appliedPromo = promo) }
                        }
                        is Sheet.Refund -> {
                            val rules = deps.api.refundRules(sheet.order.startId, token)
                            ensureActive()
                            update { copy(refundRules = rules) }
                        }
                        is Sheet.Options -> {
                            previewJobs.remove(sheet.order.id)?.cancel()
                            val options = deps.api.options(sheet.order.id, token)
                            ensureActive()
                            loadedPreviews += sheet.order.id
                            update {
                                copy(options = options, optionPreviews = optionPreviews + (sheet.order.id to options),
                                    optionPreviewErrors = optionPreviewErrors - sheet.order.id)
                            }
                        }
                        else -> Unit
                    }
                    update { copy(loading = false) }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(loading = false, error = ex.registrationError()) }
                }
            }
        }

        private fun applyPromo(promo: String) {
            val order = (state().sheet as? Sheet.Pay)?.order ?: return
            if (state().submitting) return
            sheetLoad?.cancel()
            val normalized = promo.trim().lowercase()
            update { copy(loading = true, error = null) }
            sheetLoad = scope.launch {
                try {
                    val token = token()
                    val accepted = if (normalized.isEmpty()) "" else {
                        deps.api.validatePromo(order.startId, normalized, token)?.lowercase()
                            ?: throw ServiceResponseException("Промокод не найден или больше не действует.")
                    }
                    val quote = deps.api.price(order.id, accepted, token)
                    ensureActive()
                    update { copy(price = quote, appliedPromo = accepted, loading = false) }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(loading = false, error = ex.registrationError()) }
                }
            }
        }

        private fun loadParticipants() {
            if (participantsLoad?.isActive == true) return
            update { copy(transfersLoading = true, transfersError = null) }
            participantsLoad = scope.launch {
                supervisorScope {
                    launch { deps.auth.sharedUser().getOrNull()?.let { user -> update { copy(user = user) } } }
                    try {
                        val token = token()
                        val transfers = deps.api.outgoingTransfers(token)
                        ensureActive()
                        update { copy(transfers = transfers, transfersLoading = false) }
                        if (transfers.any { it.status == "AWAITING_FEE" }) loadWalletBalance(token)
                        else update { copy(walletBalance = null, walletError = null) }
                    } catch (ex: CancellationException) {
                        throw ex
                    } catch (ex: Exception) {
                        update { copy(transfersLoading = false, transfersError = ex.registrationError()) }
                    }
                }
            }
        }

        private suspend fun loadWalletBalance(accessToken: String? = null) {
            update { copy(walletBalance = null, walletError = null) }
            try {
                val balance = deps.api.walletBalance(accessToken ?: token())
                update { copy(walletBalance = balance) }
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Exception) {
                update { copy(walletError = ex.registrationError()) }
            }
        }

        private fun loadPreview(orderId: Int) {
            if (orderId <= 0 || orderId in loadedPreviews || orderId in previewJobs) return
            val job = scope.launch(start = CoroutineStart.LAZY) {
                try {
                    val options = previewLimit.withPermit { deps.api.options(orderId, token()) }
                    ensureActive()
                    loadedPreviews += orderId
                    update { copy(optionPreviews = optionPreviews + (orderId to options), optionPreviewErrors = optionPreviewErrors - orderId) }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(optionPreviewErrors = optionPreviewErrors + (orderId to ex.registrationError())) }
                } finally {
                    if (previewJobs[orderId] == coroutineContext[Job]) previewJobs.remove(orderId)
                }
            }
            previewJobs[orderId] = job
            job.start()
        }

        private fun resetPreviews() {
            val jobs = previewJobs.values.toList()
            previewJobs.clear()
            loadedPreviews.clear()
            jobs.forEach(Job::cancel)
            update { copy(previewGeneration = previewGeneration + 1) }
        }

        private fun findRecipient(query: String) {
            val memberId = (state().sheet as? Sheet.Transfer)?.member?.id ?: return
            sheetLoad?.cancel()
            update { copy(recipient = null, loading = true, error = null) }
            sheetLoad = scope.launch {
                try {
                    val result = deps.api.recipient(memberId, query.trim(), token())
                    ensureActive()
                    update { copy(recipient = result, loading = false) }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(loading = false, error = ex.registrationError()) }
                }
            }
        }

        private fun loadExternal(more: Boolean, refresh: Boolean = false, replace: Boolean = false) {
            val current = state()
            if (more && (!current.externalHasMore || current.externalLoading)) return
            if (!replace && current.externalLoading && (!refresh || !current.externalRetryMore)) return
            externalLoad?.cancel()
            // Refresh what the user has already loaded so their scroll position stays valid.
            val targetCount = externalWindow.nextOffset
            update { copy(externalLoading = true, externalRefreshing = refresh, externalError = null, externalRetryMore = more) }
            externalLoad = scope.launch {
                try {
                    val userId = deps.auth.userID().getOrThrow()
                    val token = token()
                    var window = if (more) externalWindow else ExternalRegistrationWindow()
                    do {
                        val page = deps.api.external(userId, window.nextOffset, token)
                        ensureActive()
                        window = window.append(page)
                    } while (!more && window.hasMore && window.nextOffset < targetCount)
                    externalWindow = window
                    update {
                        copy(external = window.rows, externalHasMore = window.hasMore,
                            externalNextOffset = window.nextOffset, externalLoading = false, externalRefreshing = false)
                    }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(externalLoading = false, externalRefreshing = false, externalError = ex.registrationError()) }
                }
            }
        }

        private fun submit(command: Command) {
            val sheet = state().sheet ?: return
            if (command is Command.SaveExternal && command.draft.errors().isNotEmpty()) {
                update { copy(error = command.draft.errors().values.first()) }
                return
            }
            update { copy(submitting = true, error = null) }
            scope.launch {
                try {
                    val token = token()
                    val result = perform(command, token)
                    ensureActive()
                    val link = result.paymentUrl
                    if (link != null && !openPaymentLink(link)) return@launch
                    update {
                        copy(
                            submitting = false,
                            notice = result.message,
                            refundedOrderIds = if (command is Command.Refund) refundedOrderIds + command.orderId else refundedOrderIds,
                        )
                    }
                    when (command) {
                        is Command.DeleteExternal, is Command.SaveExternal -> {
                            if (command is Command.DeleteExternal) {
                                externalWindow = externalWindow.without(command.id)
                                update { copy(external = externalWindow.rows, externalNextOffset = externalWindow.nextOffset) }
                            }
                            close()
                            loadExternal(more = false, replace = true)
                        }
                        is Command.PayOptions, is Command.ChangeOption, is Command.PayChange -> {
                            resetPreviews()
                            if (result.paymentUrl == null) open(sheet) else close()
                            publish(Label.RegistrationChanged)
                        }
                        else -> {
                            close()
                            if (command !is Command.Correct) {
                                resetPreviews()
                                publish(Label.RegistrationChanged)
                            }
                            if (command is Command.Transfer || command is Command.RevokeTransfer || command is Command.PayTransfer) {
                                loadParticipants()
                            }
                        }
                    }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Exception) {
                    update { copy(submitting = false, error = ex.registrationError()) }
                }
            }
        }

        private fun openPaymentLink(link: String): Boolean {
            val opened = try {
                deps.actions.tryOpenWebPage(link)
            } catch (ex: CancellationException) {
                throw ex
            } catch (_: Exception) {
                false
            }
            if (!opened) update {
                copy(submitting = false, error = "Не удалось открыть страницу оплаты. Откройте её повторно.", paymentLink = link)
            }
            return opened
        }

        private data class Result(val message: String, val paymentUrl: String? = null)

        private suspend fun perform(command: Command, token: String): Result = when (command) {
            is Command.Attach -> {
                deps.api.attach(command.ids, token)
                Result("Регистрации добавлены")
            }
            is Command.SaveExternal -> {
                val draft = command.draft
                val posterId = draft.poster?.let { poster ->
                    uploadedPoster?.takeIf { it.first === poster }?.second ?: deps.profile
                        .uploadFile(poster.bytes, poster.lastModified, poster.name, poster.mimeType, token)
                        .id.also { uploadedPoster = poster to it }
                } ?: draft.posterId
                deps.api.saveExternal(draft.id, draft.body(posterId), token)
                Result(if (draft.id == null) "Регистрация добавлена" else "Изменения сохранены")
            }
            is Command.DeleteExternal -> {
                deps.api.deleteExternal(command.id, token)
                Result("Регистрация удалена")
            }
            is Command.Correct -> {
                deps.api.correct(command.correction, token)
                Result("Запрос на исправление отправлен администратору")
            }
            is Command.Transfer -> {
                deps.api.transfer(command.memberId, command.transfer, token)
                Result("Приглашение отправлено. Слот остаётся у вас до подтверждения передачи.")
            }
            is Command.Refund -> {
                deps.api.refund(command.groupId, command.percent, token)
                Result("Возврат оформлен")
            }
            is Command.PayOptions -> {
                val result = deps.api.payOptions(command.orderId, optionsPaymentBody(command.selections), token)
                requirePaymentResult(result, "Не удалось создать платёж за опции.")
                Result(if (result.paymentUrl == null) "Опции обновлены" else "Открыта страница оплаты", result.paymentUrl)
            }
            is Command.ChangeOption -> {
                deps.api.changeOption(command.orderId, requireNotNull(command.option.answerId), command.option.newAnswer(command.value), token)
                Result("Запрос на изменение опции отправлен")
            }
            is Command.PayOrder -> {
                val result = deps.api.repay(command.orderId, command.promo, token)
                val quote = state().price
                if (quote != null && quote.isPaymentRequired && quote.totalPrice > 0 && result.paymentUrl == null) {
                    throw ServiceResponseException("Сервис не вернул ссылку на оплату регистрации.")
                }
                requirePaymentResult(result, "Не удалось подтвердить оплату регистрации.")
                Result(if (result.paymentUrl == null) "Регистрация обновлена" else "Открыта страница оплаты", result.paymentUrl)
            }
            is Command.RevokeTransfer -> {
                deps.api.revokeTransfer(command.id, token)
                Result("Запрос на передачу отозван")
            }
            is Command.PayTransfer -> {
                val url = deps.api.payTransfer(command.id, command.method, token)
                if (command.method == "card" && url == null) {
                    throw ServiceResponseException("Сервис не вернул ссылку на оплату комиссии.")
                }
                Result(if (url == null) "Комиссия оплачена" else "Открыта страница оплаты комиссии", url)
            }
            is Command.PayChange -> {
                val result = deps.api.payChange(command.id, token)
                requirePaymentResult(result, "Не удалось создать платёж за изменение опции.")
                Result(if (result.paymentUrl == null) "Опция обновлена" else "Открыта страница доплаты", result.paymentUrl)
            }
        }

        private fun requirePaymentResult(result: RegistrationMutation, fallback: String) {
            if (result.paymentUrl == null && result.success != true && !result.completedWithoutRedirect) {
                throw ServiceResponseException(fallback)
            }
        }
    }
}
