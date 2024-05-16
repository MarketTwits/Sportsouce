package com.markettwits.club.common.domain.mapper

import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.workout.WorkoutRegistrationRequestRemote
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.registration.domain.WorkoutRegistrationForm

interface SubscriptionMapper {
    fun map(subscriptionsRemote: List<SubscriptionItemsRemote>): List<SubscriptionItems>
    fun map(workoutRegistrationForm: WorkoutRegistrationForm): WorkoutRegistrationRequestRemote
}