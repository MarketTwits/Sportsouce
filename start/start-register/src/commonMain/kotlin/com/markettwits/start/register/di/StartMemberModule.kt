package com.markettwits.start.register.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.members.member_root.di.rootMembersModule
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidatorBase
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStoreFactory
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStoreFactory
import com.markettwits.start.register.presentation.registration.data.StartRegistrationRepositoryBase
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegisterPriceMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegisterResultMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegistrationPageMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegistrationPromoMapper
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStoreFactory
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStoreFactory
import com.markettwits.start_cloud.di.sportSauceStartNetworkModule
import com.markettwits.teams_city.di.teamsCityModule
import com.markettwits.time.ExtendedTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val startRegistrationModule = module {
    includes(
        sportSouceNetworkModule,
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
    singleOf(::ExtendedTimeMapper) bind TimeMapper::class

    //New Register
    singleOf(::StartRegistrationPageStoreFactory)
    singleOf(::StartRegistrationRepositoryBase) bind StartRegistrationRepository::class
    singleOf(::StartRegisterPriceMapper)
    singleOf(::StartRegistrationPageMapper)
    singleOf(::StartRegisterResultMapper)
    singleOf(::StartRegistrationPromoMapper)

    singleOf(::StartPayStoreFactory)

}