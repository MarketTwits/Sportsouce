package com.markettwits.sportsouce.start.register.domain

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import kotlinx.serialization.Serializable

@Serializable
data class StartStatement(
    val userId : UserId,
    val stageId : Int?,
    val name: String,
    val surname: String,
    val birthday: String,
    val age: String,
    val email: String,
    val sex: String,
    val city: String,
    val team: String,
    val phone: String,
    val promocode: String,
    val price: Int,
    val contactPerson: Boolean = true,
    val cities: List<City>,
    val teams: List<Team>,
    val sexList: List<Sex>,
    val members : List<ProfileMember>,
    val paymentDisabled: Boolean,
    val yearDiscountApplied: Boolean,
    val distanceTitle: String,
) {

    fun getSaveAge(): Int = if (age.isEmpty()) 0 else age.toInt()

    @Serializable
    data class City(val id: Int, val name: String)

    @Serializable
    data class Team(val id: Int, val name: String)

    @Serializable
    data class Sex(val id: Int, val name: String)

    @Serializable
    sealed interface UserId{

        val userId : String

        @Serializable
        data class WithId(override val userId: String) : UserId

        @Serializable
        data object WithoutId : UserId {
            override val userId: String = "undefined"
        }
    }
}