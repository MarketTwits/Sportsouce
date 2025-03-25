package com.markettwits.sportsouce.club.registration.di

import com.arkivanov.decompose.ComponentContext
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.club.common.data.ClubRepositoryBase
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.registration.data.WorkoutRegistrationUseCaseBase
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationUseCase
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponent
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponentBase
import com.markettwits.sportsouce.club.registration.presentation.store.store.WorkoutRegistrationStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val workoutRegistrationModule = module {
    includes(com.markettwits.sportsouce.club.cloud.di.clubCloudModule, intentActionModule, authDataSourceModule)
    singleOf(::ClubRepositoryBase) bind ClubRepository::class
    singleOf(::WorkoutRegistrationStoreFactory)
    singleOf(::WorkoutRegistrationUseCaseBase) bind WorkoutRegistrationUseCase::class
}

internal fun createClubRegistrationComponent(
    storeFactory: WorkoutRegistrationStoreFactory,
    componentContext: ComponentContext,
    type: RegistrationType,
    output: (WorkoutRegistrationComponent.Output) -> Unit,
): WorkoutRegistrationComponent =
    WorkoutRegistrationComponentBase(
        componentContext = componentContext,
        type = type,
        storeFactory = storeFactory,
        output = output,
    )

