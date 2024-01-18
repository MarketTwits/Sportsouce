package com.markettwits.cloud.model.start_registration

import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterRequest(
    val alone: Boolean,
    val day: Int,
    val distance: String,
    val format: String,
    val member: List<Member>,
    val payment_disabled: Boolean?,
    val payment_type: String,
    val price: Int,
    val promocode: String?,
    val registration_without_payment: Boolean,
    val start_id: Int
){
    @Serializable
    data class Member(
        val age: Int,
        val birthday: String,
        val cite: String?,
        val city: String,
        val contactPerson: Boolean,
        val email: String,
        val fromContacts: Boolean,
        val gender: String,
        val group: Group,
        val instagram: String,
        val name: String,
        val phone: String,
        val price: Int,
        val promo: String?,
        val stage: String,
        val surname: String,
        val team: String,
        val teamNumber: Int,
        val telegram: String?,
        val type: String,
        val user_id: Int,
        val vk: String?,
        val whatsapp: String?
    )
    @Serializable
    data class Group(
        val ageFrom: String,
        val ageTo: String,
        val name: String,
        val sex: String
    )
}