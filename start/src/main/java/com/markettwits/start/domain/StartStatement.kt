package com.markettwits.start.domain

data class StartStatement(
    val name : String,
    val surname : String,
    val birthday : String,
    val age : Int,
    val email : String,
    val sex : String,
    val city : String,
    val team : String,
    val phone : String,
    val promocode : String,
    val cities : List<City>,
    val teams : List<Team>,
    val sexList : List<Sex>
){
    data class City(val id : Int, val name : String)
    data class Team(val id : Int, val name : String)
    data class Sex(val id : Int, val name : String)
}