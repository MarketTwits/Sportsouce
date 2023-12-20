package com.markettwits.starts

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.sportsourcedemo.all.Row

sealed class StartsUiState {
    class Success(
        val items: List<List<StartsListItem>>,
    ) : StartsUiState(){
    }
    class Failed(val message: String) : StartsUiState()
    data object Loading : StartsUiState()
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
sealed class StartScreenBoard {
    class Actual(val items : List<StartsListItem>) : StartScreenBoard()
    class Past(val items : List<StartsListItem>): StartScreenBoard()
    class Review(val items : List<StartsListItem>): StartScreenBoard()
    data object MyPanel : StartScreenBoard()
}
