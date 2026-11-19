package com.markettwits.sportsouce.starts.albums.filter.store

import com.arkivanov.mvikotlin.core.store.Store

interface AlbumsFilterStore : Store<AlbumsFilterStore.Intent, AlbumsFilterStore.State, AlbumsFilterStore.Label> {
    data class State(val year: Int? = null)
    sealed interface Intent {
        data class SelectYear(val year: Int?) : Intent
        data object Reset : Intent
        data object Apply : Intent
        data object Dismiss : Intent
    }
    sealed interface Label {
        data class Apply(val year: Int?) : Label
        data object Dismiss : Label
    }
}
