package com.markettwits.sportsouce.start.register.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.profile.members.member_root.di.rootMembersModule
import com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule
import com.markettwits.sportsouce.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.sportsouce.start.register.presentation.member.domain.RegistrationMemberValidatorBase
import com.markettwits.sportsouce.start.register.presentation.member.store.RegistrationMemberStoreFactory
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStoreFactory
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.StartRegistrationRepositoryBase
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegisterPriceMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegisterResultMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegistrationPageMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegistrationPromoMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStoreFactory
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStoreFactory
import com.markettwits.sportsouce.teams_city.di.teamsCityModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val startRegistrationModule = module {
    includes(
        sportSauceStartNetworkModule,
        teamsCityModule,
        rootMembersModule,
        intentActionModule,
        crashlyticsModule
    )
    //StoreFactory
    singleOf(::RegistrationPromoStoreFactory)
    singleOf(::RegistrationMemberStoreFactory)
    //Repository
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::RegistrationMemberValidatorBase) bind RegistrationMemberValidator::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    //New Register
    singleOf(::StartRegistrationPageStoreFactory)
    singleOf(::StartRegistrationRepositoryBase) bind StartRegistrationRepository::class
    singleOf(::StartRegisterPriceMapper)
    singleOf(::StartRegistrationPageMapper)
    singleOf(::StartRegisterResultMapper)
    singleOf(::StartRegistrationPromoMapper)

    singleOf(::StartPayStoreFactory)

}