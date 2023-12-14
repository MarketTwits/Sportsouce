package com.markettwits.starts

sealed class StartsUiState {
    class Success(val items: List<StartsListItem>) : StartsUiState()
    class Failed(val message: String) : StartsUiState()
    object Loading : StartsUiState()
}

data class StartsListItem(
    val id: Int,
    val name: String,
    val image : String,
    val date: String,
    val statusCode : StatusCode,
    val place: String,
    val distance: String
){
    data class StatusCode(val id : Int, val message: String)
}
