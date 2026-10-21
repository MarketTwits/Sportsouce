package com.markettwits.sportsouce.starts.albums.filter.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.starts.albums.filter.store.AlbumsFilterStore.*

internal class AlbumsFilterStoreFactory(private val storeFactory: StoreFactory) {
    fun create(year: Int?): AlbumsFilterStore = object : AlbumsFilterStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "AlbumsFilterStore", initialState = State(year), executorFactory = ::ExecutorImpl, reducer = ReducerImpl,
        ) {}
    private data class YearChanged(val year: Int?)
    private class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, YearChanged, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.SelectYear -> dispatch(YearChanged(intent.year))
                Intent.Reset -> dispatch(YearChanged(null))
                Intent.Apply -> publish(Label.Apply(state().year))
                Intent.Dismiss -> publish(Label.Dismiss)
            }
        }
    }
    private object ReducerImpl : Reducer<State, YearChanged> {
        override fun State.reduce(msg: YearChanged) = copy(year = msg.year)
    }
}
