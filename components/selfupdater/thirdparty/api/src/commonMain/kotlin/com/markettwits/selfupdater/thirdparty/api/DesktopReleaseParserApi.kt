package com.markettwits.selfupdater.thirdparty.api

enum class DesktopPlatform { Windows, MacOS }
enum class DesktopArchitecture { X64, Arm64 }

interface DesktopReleaseParserApi {
    /** Null means that this release does not provide a compatible desktop package. */
    suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture): SelfUpdate?
}
