package com.markettwits.sportsouce.starts.favorites.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.data.StartFavoritesRepositoryBase
import com.markettwits.sportsouce.starts.favorites.domain.StartFavoritesRepository
import com.markettwits.sportsouce.starts.favorites.presentation.component.StartsFavoritesComponent
import com.markettwits.sportsouce.starts.favorites.presentation.component.StartsFavoritesComponentBase
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsFavoritesStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

val startsFavoritesModule = module {
    includes(startsCommonModule, authDataSourceModule)

    // Store factory
    singleOf<StoreFactory>(::DefaultStoreFactory)
    singleOf(::StartFavoritesRepositoryBase) bind StartFavoritesRepository::class
    factory {
        StartsFavoritesStoreFactory(
            storeFactory = get<StoreFactory>(),
            repository = get()
        )
    }
}

fun Scope.createStartsFavoritesComponent(
    componentContext: ComponentContext,
    output: (StartsListItem) -> Unit,
    pop: () -> Unit,
): StartsFavoritesComponent =
    StartsFavoritesComponentBase(
        componentContext = componentContext,
        storeFactory = get(),
        pop = pop,
        start = output,
    )

