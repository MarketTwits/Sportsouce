package com.markettwits.sportsouce.profile.results.presentation.store

import com.arkivanov.mvikotlin.core.store.*
import com.markettwits.sportsouce.profile.results.domain.MyResultsRepository
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.*

internal class MyResultsStoreFactory(private val storeFactory: StoreFactory, private val repository: MyResultsRepository) {
    fun create(): MyResultsStore = object : MyResultsStore, Store<Intent, State, Label> by storeFactory.create(
        name = "MyResultsStore", initialState = State(), bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { MyResultsExecutor(repository) }, reducer = MyResultsReducer,
    ) {}
}
