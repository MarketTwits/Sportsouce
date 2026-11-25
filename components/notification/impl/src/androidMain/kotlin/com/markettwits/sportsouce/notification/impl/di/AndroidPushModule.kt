package com.markettwits.sportsouce.notification.impl.di

import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.service.api.AuthSessionEnding
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences
import com.markettwits.sportsouce.notification.impl.data.PushDeviceApi
import com.markettwits.sportsouce.notification.impl.domain.PushDeviceRepository
import com.markettwits.sportsouce.notification.impl.domain.PushPermission
import com.markettwits.sportsouce.notification.impl.domain.PushRegistration
import com.markettwits.sportsouce.notification.impl.push.AndroidPushNotifications
import com.markettwits.sportsouce.notification.impl.push.AndroidPushPermission
import com.markettwits.sportsouce.notification.impl.push.AndroidPushPlatform
import com.markettwits.sportsouce.notification.impl.push.PushNotificationDisplay
import com.markettwits.sportsouce.notification.impl.push.PushDevicePreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val androidPushModule = module {
    includes(authDataSourceModule)
    single<PushDeviceRepository> { PushDeviceApi(SportSauceHttpClientProvider()) }
    single { PushDevicePreference(androidContext()) }
    single { PushNotificationDisplay(androidContext(), get()) }
    single { AndroidPushPlatform(androidContext(), get(), get()) }
    single { AndroidPushPermission(androidContext(), get(), get(), get(), get()) }.apply {
        bind(PushPermission::class)
        bind(PushNotificationPreferences::class)
    }
    single {
        PushRegistration(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
            sessions = get(),
            renewAccessToken = { get<AuthDataSource>().updateToken().getOrThrow() },
            devices = get(),
            platform = get<AndroidPushPlatform>(),
        )
    }
    single { AndroidPushNotifications(get(), get()) }
    single<AuthSessionEnding> { AuthSessionEnding { session -> get<AndroidPushNotifications>().beforeSignOut(session) } }
}
