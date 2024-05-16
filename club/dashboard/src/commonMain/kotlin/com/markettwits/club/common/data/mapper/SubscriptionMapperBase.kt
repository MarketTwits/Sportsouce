package com.markettwits.club.common.data.mapper

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.subscription.SubscriptionRemote
import com.markettwits.club.cloud.models.workout.WorkoutRegistrationRequestRemote
import com.markettwits.club.common.domain.mapper.SubscriptionMapper
import com.markettwits.club.dashboard.domain.Subscription
import com.markettwits.club.dashboard.domain.SubscriptionItems
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
            workoutId = workoutRegistrationForm.workoutId,
            name = workoutRegistrationForm.name,
            surname = workoutRegistrationForm.surname,
            phone = workoutRegistrationForm.phone
        )

    private fun mapSubscriptions(
        subscriptionsRemote: List<SubscriptionRemote>
    ): List<Subscription> = subscriptionsRemote.map {
        Subscription(
            description = it.description,
            discount = it.discount,
            name = it.name,
            id = it.id,
            type = it.type,
            price = it.price
        )
    }
}