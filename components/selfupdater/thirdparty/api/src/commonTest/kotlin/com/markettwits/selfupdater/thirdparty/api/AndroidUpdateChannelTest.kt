package com.markettwits.selfupdater.thirdparty.api

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidUpdateChannelTest {
    @Test
    fun playInstallerTakesPrecedenceAndLegacyDefaultsToPlay() {
        assertEquals(AndroidUpdateChannel.GooglePlay, selectAndroidUpdateChannel("github", "com.android.vending"))
        assertEquals(AndroidUpdateChannel.GooglePlay, selectAndroidUpdateChannel("googlePlay", null))
        assertEquals(AndroidUpdateChannel.GooglePlay, selectAndroidUpdateChannel(null, null))
        assertEquals(AndroidUpdateChannel.GooglePlay, selectAndroidUpdateChannel(null, "com.android.packageinstaller"))
    }

    @Test
    fun onlyAnExplicitGithubBuildUsesGithubForSideloads() {
        assertEquals(AndroidUpdateChannel.GitHub, selectAndroidUpdateChannel("github", null))
        assertEquals(AndroidUpdateChannel.GitHub, selectAndroidUpdateChannel("github", "com.android.packageinstaller"))
        assertEquals(AndroidUpdateChannel.GooglePlay, selectAndroidUpdateChannel("unknown", "other.store"))
    }
}
