package com.markettwits.selfupdater.thirdparty.github.di

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.selfupdater.thirdparty.api.SelfUpdateParserApi
import com.markettwits.selfupdater.thirdparty.github.parser.GithubParser
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val thirdPartyGitHubModule = module {
    singleOf(::GithubParser) bind SelfUpdateParserApi::class
    single<HttpClient> {
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            baseUrl = "https://api.github.com"
        ).provide(false)
    }
}