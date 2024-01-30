package presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core_ui.coroutineComponent.CoroutineComponent
import com.markettwits.core_ui.extensions.collectAsScope
import com.markettwits.random_user.RandomUser
import data.RandomListUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import presentation.store.ListStore
import presentation.store.ListStoreStoreFactory

class ListComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ListStoreStoreFactory,
    private val repository: RandomListUserRepository,
    private val scope: CoroutineScope,
    private val onClickItem: (RandomUser) -> Unit
) : ListComponent, CoroutineComponent(componentContext, scope) {

    private val store = instanceKeeper.getStore {
        storeFactory.create(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ListStore.State>
        get() = store.stateFlow

    override fun obtainEvent(intent: ListStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.collectAsScope(scope) {
            if (it is ListStore.Label.OnClickItem)
                onClickItem(it.user)
        }
    }
}
