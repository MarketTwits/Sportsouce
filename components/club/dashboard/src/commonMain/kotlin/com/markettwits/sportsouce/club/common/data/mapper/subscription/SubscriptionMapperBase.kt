package com.markettwits.sportsouce.club.common.data.mapper.subscription

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionRemote
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceRequest
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceResponse
import com.markettwits.sportsouce.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote
import com.markettwits.sportsouce.club.dashboard.domain.Subscription
import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.sportsouce.club.registration.domain.normalizeClubPhone

class SubscriptionMapperBase : SubscriptionMapper {
    override fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems> =
        subscriptionsRemote.filterNot { it.isHidden }.sortedBy { it.order }.map {
            SubscriptionItems(
                it.type ?: it.name,
                mapSubscriptions(it.subscriptions ?: it.subscription)
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

    override fun map(workoutPriceForm: WorkoutPriceForm): WorkoutPriceRequest = WorkoutPriceRequest(
        count = workoutPriceForm.count,
        id = workoutPriceForm.id,
        type = workoutPriceForm.type
    )

    override fun map(workoutPriceResponse: WorkoutPriceResponse): WorkoutPrice = WorkoutPrice(
        priceWithoutDiscounts = workoutPriceResponse.priceWithoutDiscounts,
        totalPrice = workoutPriceResponse.totalPrice
    )

    override fun map(sharedUser: Result<SharedUser>): WorkoutRegistrationForm =
        sharedUser.fold(
            onSuccess = {
                WorkoutRegistrationForm(
                    type = RegistrationType.Empty,
                    name = it.name,
                    surname = it.surname,
                    phone = it.phone
                )
            },
            onFailure = {
                WorkoutRegistrationForm.EMPTY
            }
        )

    private fun mapSubscriptions(
        subscriptionsRemote: List<SubscriptionRemote>
    ): List<Subscription> = subscriptionsRemote.sortedBy { it.order }.map {
        Subscription(
            description = it.description,
            discount = it.discount ?: 0,
            name = it.name,
            id = it.id,
            type = it.type,
            price = it.price,
            priceDependsOnCount = it.isPriseDependsOnCount,
            maxAmount = it.maxAmount ?: 1,
            color = it.color
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

    private fun mapPhone(phone: String): String = requireNotNull(normalizeClubPhone(phone)) {
        "Введите корректный номер телефона : +7 (000) 000-00-00"
    }
}
