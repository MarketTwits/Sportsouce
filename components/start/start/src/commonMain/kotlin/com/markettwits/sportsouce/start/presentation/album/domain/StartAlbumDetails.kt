package com.markettwits.sportsouce.start.presentation.album.domain

data class StartAlbumDetails(
    val name: String,
    val startName: String,
    val date: String,
    val photosCount: Int,
    val tags: List<String>,
)

data class AlbumPhoto(val id: Int, val url: String)
data class AlbumPhotosPage(val rows: List<AlbumPhoto>, val count: Int)
