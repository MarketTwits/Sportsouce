package com.markettwits.start.presentation.membres.list

import kotlinx.serialization.Serializable

//@Serializable
//data class StartMembersUi(
//    val id: Int,
//    val name: String,
//    val surname : String,
//    val distance: String,
//    val team: String,
//    val group : String,
//    val city: String
//)
@Serializable
sealed interface StartMembersUi{
    val distance: String
    val team: String
    val group : String
    val city: String
    @Serializable
    data class Single(
        val id: Int,
        val name: String,
        val surname : String,
        override val distance: String,
        override val team: String,
        override val group : String,
        override val city: String
    ) : StartMembersUi
    @Serializable
    data class Team(
        val members : List<TeamMember>,
        override val distance: String,
        override val team: String,
        override val group : String,
        override val city: String
    ) : StartMembersUi
    @Serializable
    data class TeamMember(
        val memberId : Int,
        val name: String,
        val surname : String,
    )

}