package com.markettwits.start.register.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.members.member_root.di.rootMembersModule
import com.markettwits.start.register.data.registration.RegistrationStartRepository
import com.markettwits.start.register.data.registration.RegistrationStartRepositoryBase
import com.markettwits.start.register.data.registration.mapper.RegistrationMapper
import com.markettwits.start.register.data.registration.mapper.RegistrationMapperBase
import com.markettwits.start.register.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.register.data.registration.mapper.RegistrationPromoMapperBase
import com.markettwits.start.register.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.register.data.registration.mapper.RegistrationRemoteToDomainMapperBase
import com.markettwits.start.register.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.register.data.registration.mapper.RegistrationResponseMapperBase
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidatorBase
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStoreFactory
import com.markettwits.start.register.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.register.presentation.order.domain.interactor.OrderInteractorBase
import com.markettwits.start.register.presentation.order.domain.validation.OrderValidation
import com.markettwits.start.register.presentation.order.domain.validation.OrderValidationBase
import com.markettwits.start.register.presentation.order.presentation.store.OrderStoreExecutorHandle
import com.markettwits.start.register.presentation.order.presentation.store.OrderStoreExecutorHandleBase
import com.markettwits.start.register.presentation.order.presentation.store.OrderStoreFactory
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStoreFactory
import com.markettwits.teams_city.di.teamsCityModule
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val startRegistrationModule = module {
    includes(sportSouceNetworkModule, timeApiNetworkModule, teamsCityModule, rootMembersModule)
    //StoreFactory
    singleOf(::RegistrationPromoStoreFactory)
    singleOf(::RegistrationMemberStoreFactory)
    singleOf(::OrderStoreFactory)
    singleOf(::OrderStoreExecutorHandleBase) bind OrderStoreExecutorHandle::class
    //Interactor
    singleOf(::OrderInteractorBase) bind OrderInteractor::class
    singleOf(::OrderValidationBase) bind OrderValidation::class
    //Repository
    singleOf(::RegistrationStartRepositoryBase) bind RegistrationStartRepository::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::RegistrationMemberValidatorBase) bind RegistrationMemberValidator::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::RegistrationRemoteToDomainMapperBase) bind RegistrationRemoteToDomainMapper::class
    singleOf(::RegistrationMapperBase) bind RegistrationMapper::class
    singleOf(::RegistrationResponseMapperBase) bind RegistrationResponseMapper::class
    singleOf(::RegistrationPromoMapperBase) bind RegistrationPromoMapper::class

}