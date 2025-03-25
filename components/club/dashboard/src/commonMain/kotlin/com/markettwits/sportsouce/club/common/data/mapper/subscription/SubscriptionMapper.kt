package com.markettwits.sportsouce.club.common.data.mapper.subscription

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceRequest
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceResponse
import com.markettwits.sportsouce.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote
import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm

interface SubscriptionMapper {

    fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems>

    fun map(workoutRegistrationForm: WorkoutRegistrationForm): WorkoutRegistrationRequestRemote

    fun map(workoutPriceForm: WorkoutPriceForm) : WorkoutPriceRequest

    fun map(workoutPriceResponse: WorkoutPriceResponse) : WorkoutPrice

    fun map(sharedUser: Result<SharedUser>) : WorkoutRegistrationForm
}