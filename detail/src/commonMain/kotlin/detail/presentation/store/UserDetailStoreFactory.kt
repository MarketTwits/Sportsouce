package detail.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import detail.presentation.store.UserDetailStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.random_user.RandomUser
import detail.domain.LaunchFeature
import detail.presentation.store.UserDetailStore.Label
import detail.presentation.store.UserDetailStore.State

class UserDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val feature: LaunchFeature) {
    fun create(item: RandomUser): UserDetailStore = UserDetailStoreImpl(item)
    private inner class UserDetailStoreImpl(private val item: RandomUser) :
        UserDetailStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "UserDetailStoreStore",
            initialState = UserDetailStore.State(item = item),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { UserDetailStoreExecutor(feature) },
            reducer = UserDetailStoreReducer
        )
}