package com.markettwits.club.common.data.mapper.subscription

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.workout.price.WorkoutPriceRequest
import com.markettwits.club.cloud.models.workout.price.WorkoutPriceResponse
import com.markettwits.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.registration.domain.WorkoutPrice
import com.markettwits.club.registration.domain.WorkoutPriceForm
import com.markettwits.club.registration.domain.WorkoutRegistrationForm

interface SubscriptionMapper {

    fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems>

    fun map(workoutRegistrationForm: WorkoutRegistrationForm): WorkoutRegistrationRequestRemote

    fun map(workoutPriceForm: WorkoutPriceForm) : WorkoutPriceRequest

    fun map(workoutPriceResponse: WorkoutPriceResponse) : WorkoutPrice
}