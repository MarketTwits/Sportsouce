package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain

import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException
import com.markettwits.activityholder.CurrentActivityHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal class AndroidRegisteredCredentialSaver : RegisteredCredentialSaver {
    override suspend fun save(phone: String, password: String) {
        val activity = CurrentActivityHolder.getCurrentActivity() ?: return
        val credentialManager = CredentialManager.create(activity)

        try {
            credentialManager.createCredential(
                context = activity,
                request = CreatePasswordRequest(
                    id = phone,
                    password = password
                )
            )
        } catch (_: CreateCredentialException) {
            // Ignore provider cancellation/unavailability and continue app flow.
        }
    }
}

internal actual val signUpCredentialSaverModule = module {
    singleOf(::AndroidRegisteredCredentialSaver) bind RegisteredCredentialSaver::class
}
