package com.markettwits.starts

import androidx.compose.runtime.Immutable
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.sportsourcedemo.all.Row
@Immutable
sealed class StartsUiState {
    class Success(
        val items: List<List<StartsListItem>>,
    ) : StartsUiState(){
    }
    class Failed(val message: String) : StartsUiState()
    data object Loading : StartsUiState()
}
@Immutable
data class StartsListItem(
    val id: Int,
    val name: String,
    val image : String,
    val date: String,
    val statusCode : StatusCode,
    val place: String,
    val distance: String
){
    @Immutable
    data class StatusCode(val id : Int, val message: String)
}