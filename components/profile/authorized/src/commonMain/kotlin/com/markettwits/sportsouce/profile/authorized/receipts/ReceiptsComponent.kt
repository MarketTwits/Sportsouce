package com.markettwits.sportsouce.profile.authorized.receipts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.sportsouce.profile.cloud.model.receipts.PaidReceipt
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ReceiptsComponent(
    context: ComponentContext,
    repository: ReceiptsRepository,
    val back: () -> Unit,
    val open: (PaidReceipt) -> Unit,
) : ComponentContext by context {
    private val controller = instanceKeeper.getOrCreate { ReceiptLoadController(load = repository::receipts) }
    val state get() = controller.state
    fun reload() = controller.reload()
}

class ReceiptDocumentComponent(
    context: ComponentContext,
    repository: ReceiptsRepository,
    receiptId: Int,
    val back: () -> Unit,
) : ComponentContext by context {
    private val controller = instanceKeeper.getOrCreate { ReceiptLoadController { repository.document(receiptId) } }
    val state get() = controller.state
    fun reload() = controller.reload()
}

data class ReceiptLoadState<T>(val data: T? = null, val loading: Boolean = false, val error: String? = null)

internal class ReceiptLoadController<T>(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val load: suspend () -> T,
) : InstanceKeeper.Instance {
    private val mutableState = MutableStateFlow(ReceiptLoadState<T>())
    val state = mutableState.asStateFlow()

    init { reload() }

    fun reload() {
        if (state.value.loading) return
        mutableState.update { it.copy(loading = true, error = null) }
        scope.launch {
            try {
                val result = load()
                ensureActive()
                mutableState.value = ReceiptLoadState(data = result)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                val message = error.networkExceptionHandler().message ?: "Не удалось загрузить данные"
                ensureActive()
                mutableState.update { it.copy(loading = false, error = message) }
            }
        }
    }

    override fun onDestroy() { scope.cancel() }
}
