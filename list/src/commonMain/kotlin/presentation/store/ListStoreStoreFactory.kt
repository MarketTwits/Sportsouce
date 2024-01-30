package presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import presentation.store.ListStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import data.RandomListUserRepository
import presentation.store.ListStore.Label
import presentation.store.ListStore.State

class ListStoreStoreFactory(private val storeFactory: StoreFactory) {

    fun create(repository: RandomListUserRepository): ListStore = ListStoreImpl(repository)

    private inner class ListStoreImpl(
        private val repository: RandomListUserRepository
    ) :
        ListStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ListStoreStore",
            initialState = ListStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ListStoreExecutor(repository = repository) },
            reducer = ListStoreReducer
        )
}