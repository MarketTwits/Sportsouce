package com.markettwits.selfupdater.thirdparty.github.parser

import com.markettwits.buildkonfig.isDebugMode

actual fun GithubParser.isDev(): Boolean = isDebugMode
