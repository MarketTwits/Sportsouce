package com.markettwits.selfupdater.thirdparty.api

enum class AndroidUpdateChannel { GooglePlay, GitHub }

/** Legacy builds default to Play; only an explicitly marked GitHub build can use sideload updates. */
fun selectAndroidUpdateChannel(buildChannel: String?, installerPackage: String?): AndroidUpdateChannel = when {
    installerPackage == "com.android.vending" -> AndroidUpdateChannel.GooglePlay
    buildChannel == "github" -> AndroidUpdateChannel.GitHub
    else -> AndroidUpdateChannel.GooglePlay
}
