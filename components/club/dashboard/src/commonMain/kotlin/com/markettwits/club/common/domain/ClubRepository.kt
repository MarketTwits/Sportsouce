package com.markettwits.club.common.domain

import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.registration.domain.WorkoutPrice
import com.markettwits.club.registration.domain.WorkoutPriceForm
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import kotlinx.coroutines.flow.Flow

interface ClubRepository {

    suspend fun subscriptions(): Flow<List<SubscriptionItems>>

    suspend fun workoutRegistrationUserData() : WorkoutRegistrationForm

    suspend fun workoutRegistration(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit>

    suspend fun workoutRegistrationPrice(workoutPriceForm: WorkoutPriceForm) : Result<WorkoutPrice>

    suspend fun clubInfo(): Result<List<ClubInfo>>
}