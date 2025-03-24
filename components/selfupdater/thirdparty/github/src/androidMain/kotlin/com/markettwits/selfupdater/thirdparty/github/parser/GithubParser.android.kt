package com.markettwits.selfupdater.thirdparty.github.parser

import com.markettwits.selfupdater.thirdparty.github.BuildConfig

actual fun GithubParser.isDev(): Boolean = BuildConfig.DEBUG