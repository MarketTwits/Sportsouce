package com.markettwits.club.common.data.mapper

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.subscription.SubscriptionRemote
import com.markettwits.club.cloud.models.workout.WorkoutRegistrationRequestRemote
import com.markettwits.club.common.domain.mapper.SubscriptionMapper
import com.markettwits.club.dashboard.domain.Subscription
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.club.registration.domain.WorkoutRegistrationForm

class SubscriptionMapperBase : SubscriptionMapper {
    override fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems> =
        subscriptionsRemote.map {
            SubscriptionItems(
                it.name,
                mapSubscriptions(it.subscription)
            )
        }.filter { it.subscription.isNotEmpty() }

    override fun map(workoutRegistrationForm: WorkoutRegistrationForm): WorkoutRegistrationRequestRemote =
        WorkoutRegistrationRequestRemote(
            id = workoutRegistrationForm.type.id,
            count = mapSubscriptionCount(workoutRegistrationForm),
            type = mapSubscriptionType(workoutRegistrationForm),
            name = workoutRegistrationForm.name,
            surname = workoutRegistrationForm.surname,
            phone = mapPhone(workoutRegistrationForm.phone)
        )

    private fun mapSubscriptions(
        subscriptionsRemote: List<SubscriptionRemote>
    ): List<Subscription> = subscriptionsRemote.map {
        Subscription(
            description = it.description,
            discount = it.discount ?: 0,
            name = it.name,
            id = it.id,
            type = it.type,
            price = it.price
        )
    }

    private fun mapSubscriptionType(workoutRegistrationForm: WorkoutRegistrationForm): String =
        when (workoutRegistrationForm.type) {
            is RegistrationType.Schedule -> "schedule"
            is RegistrationType.Subscription -> "subscription"
            is RegistrationType.Trainer -> "trainer"
            is RegistrationType.Workout -> "workout"
            is RegistrationType.Empty -> throw IllegalStateException("Request should contains non Empty Request exception")
        }

    private fun mapSubscriptionCount(workoutRegistrationForm: WorkoutRegistrationForm): Int? =
        if (workoutRegistrationForm.type is RegistrationType.Subscription) workoutRegistrationForm.type.count else null

    private fun mapPhone(phone: String): String = phone.replace(Regex("\\D"), "")
}