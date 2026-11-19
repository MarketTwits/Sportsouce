package com.markettwits.selfupdater.thirdparty.github.di

import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core_cloud.provider.defaultHttpClientEngine
import com.markettwits.selfupdater.thirdparty.api.SelfUpdateParserApi
import com.markettwits.selfupdater.thirdparty.api.DesktopReleaseParserApi
import com.markettwits.selfupdater.thirdparty.github.parser.GithubParser
import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val thirdPartyGitHubModule = module {
    single { GithubParser(get(named("selfUpdaterGithubClient"))) }
    single<SelfUpdateParserApi> { get<GithubParser>() }
    single<DesktopReleaseParserApi> { get<GithubParser>() }
    single<HttpClient>(named("selfUpdaterGithubClient")) {
        HttpClient(defaultHttpClientEngine()) { configureGithubClient() }
    }
}

internal fun HttpClientConfig<*>.configureGithubClient() {
    expectSuccess = true
    install(ContentNegotiation) { json(JsonProviderBase().provide()) }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
        socketTimeoutMillis = 30_000
    }
    defaultRequest { header(HttpHeaders.Accept, "application/vnd.github+json") }
}
