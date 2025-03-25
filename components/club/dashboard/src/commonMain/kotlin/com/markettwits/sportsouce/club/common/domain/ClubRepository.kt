package com.markettwits.sportsouce.club.common.domain

import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import kotlinx.coroutines.flow.Flow

interface ClubRepository {

    suspend fun subscriptions(): Flow<List<SubscriptionItems>>

    suspend fun workoutRegistrationUserData() : WorkoutRegistrationForm

    suspend fun workoutRegistration(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit>

    suspend fun workoutRegistrationPrice(workoutPriceForm: WorkoutPriceForm) : Result<WorkoutPrice>

    suspend fun clubInfo(): Result<List<ClubInfo>>
}