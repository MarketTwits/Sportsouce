package com.markettwits.selfupdater.thirdparty.api

import androidx.core.content.FileProvider

/** Separate provider keeps update files isolated from the media sharing provider. */
class SelfUpdaterFileProvider : FileProvider()
