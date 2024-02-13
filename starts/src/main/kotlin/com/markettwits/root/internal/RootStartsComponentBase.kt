package com.markettwits.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.provider.HttpClientProvider
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.root.api.RootStartsComponent
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_search.root.RootStartsSearchComponentBase
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.data.StartsMainCache
import com.markettwits.starts.data.StartsRepositoryBase
import com.markettwits.starts.presentation.StartsScreenComponent
import com.markettwits.starts_common.data.StartsCloudToListMapperBase
import ru.alexpanov.core_network.provider.JsonProvider

class RootStartsComponentBase(
    componentContext: ComponentContext,
    //   private val launchPolicy: RootStartsComponent.LaunchPolicy = RootStartsComponent.LaunchPolicy.Main
) : RootStartsComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<RootStartsComponent.Config>()

    override val configStack =
        childStack(
            source = navigation,
            serializer = RootStartsComponent.Config.serializer(),
            initialConfiguration = RootStartsComponent.Config.Starts,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: RootStartsComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsComponent.Child =
        when (config) {
            is RootStartsComponent.Config.Start -> RootStartsComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Starts ->
                RootStartsComponent.Child.Starts(StartsScreenComponent(
                    componentContext = componentContext,
                    dataSource = StartsRepositoryBase(
                        service = StartsRemoteDataSourceImpl(
                            HttpClientProvider(
                                JsonProvider().get(),
                                "https://sport-73zoq.ondigitalocean.app"
                            )
                        ),
                        cache = StartsMainCache(),
                        mapper = StartsCloudToUiMapper.Base(
                            StartsCloudToListMapperBase(BaseTimeMapper())
                        )
                    ),
                    //   launchPolicy = launchPolicy,
                    toDetail = {
                        onItemClick(it)
                    },
                    toSearch = {
                        navigation.push(RootStartsComponent.Config.Search)
                    }
                ))
            RootStartsComponent.Config.Search -> RootStartsComponent.Child.Search(
                RootStartsSearchComponentBase(
                    context = componentContext,
                    pop = navigation::pop
                )
            )
        }

//    private fun repository(launchPolicy: RootStartsComponent.LaunchPolicy): StartsRepository {
//
//        return when (launchPolicy) {
//            is RootStartsComponent.LaunchPolicy.Main -> StartsRepositoryBase(
//                StartsRemoteDataSourceImpl(
//                    HttpClientProvider(
//                        JsonProvider().get(),
//                        "https://sport-73zoq.ondigitalocean.app"
//                    )
//                ),
//                StartsCloudToUiMapper.Base(
//                    StartsCloudToListMapperBase( BaseTimeMapper())
//                )
//            )
//
//            is RootStartsComponent.LaunchPolicy.WithFilter -> StartsRepositoryWithFilter(
//                StartsRemoteDataSourceImpl(
//                    HttpClientProvider(
//                        JsonProvider().get(),
//                        "https://sport-73zoq.ondigitalocean.app"
//                    )
//                ),
//                StartsCloudToUiMapper.Base(
//                    StartsCloudToListMapperBase( BaseTimeMapper())
//                )
//            )
//        }
    //   }

    fun onItemClick(startdId: Int) {
        navigation.push(RootStartsComponent.Config.Start(startdId))
    }
}
