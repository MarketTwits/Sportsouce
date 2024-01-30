package detail.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core_ui.coroutineComponent.CoroutineComponent
import com.markettwits.core_ui.extensions.collectAsScope
import com.markettwits.random_user.RandomUser
import detail.presentation.store.UserDetailStore
import detail.presentation.store.UserDetailStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class UserDetailComponentBase(
    componentContext: ComponentContext,
    private val item: RandomUser,
    private val storeFactory: UserDetailStoreFactory,
    private val scope: CoroutineScope,
    private val pop: () -> Unit,
) : UserDetailComponent, CoroutineComponent(componentContext, scope) {

    private val store = instanceKeeper.getStore {
        storeFactory.create(item)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<UserDetailStore.State>
        get() = store.stateFlow

    override fun obtainEvent(intent: UserDetailStore.Intent) {
        store.accept(intent)
    }

    private val backCallback = BackCallback { pop() }

    init {
        backHandler.register(backCallback)
        store.labels.collectAsScope(scope) {
            when (it) {
                is UserDetailStore.Label.OnBackClicked -> pop()
            }
        }
    }
}
