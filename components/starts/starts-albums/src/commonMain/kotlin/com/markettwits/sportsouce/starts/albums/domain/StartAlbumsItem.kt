package com.markettwits.sportsouce.starts.albums.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartAlbumsItem(val id: Int, val name: String, val date: String, val albums: List<Album>) {
    @Serializable
    data class Album(val id: Int, val name: String, val photosCount: Int, val previewUrl: String)
}

data class StartAlbumsPage(val rows: List<StartAlbumsItem>, val count: Int)
