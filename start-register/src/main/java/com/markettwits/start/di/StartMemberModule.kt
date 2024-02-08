package com.markettwits.start.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.data.registration.RegistrationStartRepositoryBase
import com.markettwits.start.data.registration.mapper.RegistrationMapper
import com.markettwits.start.data.registration.mapper.RegistrationMapperBase
import com.markettwits.start.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.data.registration.mapper.RegistrationPromoMapperBase
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapperBase
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapperBase
import com.markettwits.start.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.presentation.member.domain.RegistrationMemberValidatorBase
import com.markettwits.start.presentation.member.store.RegistrationMemberStoreFactory
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractorBase
import com.markettwits.start.presentation.order.domain.validation.OrderValidation
import com.markettwits.start.presentation.order.domain.validation.OrderValidationBase
import com.markettwits.start.presentation.order.store.OrderStoreExecutorHandle
import com.markettwits.start.presentation.order.store.OrderStoreExecutorHandleBase
import com.markettwits.start.presentation.order.store.OrderStoreFactory
import com.markettwits.start.presentation.promo.store.RegistrationPromoStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val startRegistrationModule = module{
    includes(sportSouceNetworkModule, timeApiNetworkModule)
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