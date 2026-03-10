package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain

import org.koin.core.module.Module

internal interface RegisteredCredentialSaver {
    suspend fun save(phone: String, password: String)
}

internal class NoOpRegisteredCredentialSaver : RegisteredCredentialSaver {
    override suspend fun save(phone: String, password: String) = Unit
}

internal expect val signUpCredentialSaverModule: Module
