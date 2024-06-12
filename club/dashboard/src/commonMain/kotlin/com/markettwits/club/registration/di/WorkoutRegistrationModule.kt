package com.markettwits.club.registration.di

import com.arkivanov.decompose.ComponentContext
import com.markettwits.club.cloud.di.clubCloudModule
import com.markettwits.club.common.data.ClubRepositoryBase
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.registration.data.WorkoutRegistrationUseCaseBase
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.club.registration.domain.WorkoutRegistrationUseCase
import com.markettwits.club.registration.presentation.component.WorkoutRegistrationComponent
import com.markettwits.club.registration.presentation.component.WorkoutRegistrationComponentBase
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStoreFactory
import com.markettwits.intentActionModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val workoutRegistrationModule = module {
    includes(clubCloudModule, intentActionModule)
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

