package com.markettwits.version

import android.content.Context
import android.os.Build


class ApplicationVersionManagerAndroid(
    context: Context
) : ApplicationVersionManager {

    private val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    override fun currentDistribution(): ApplicationVersion =
        ApplicationVersion(
            versionName = packageInfo.versionName,
            versionBuildNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                packageInfo.longVersionCode.toString()
            else
                packageInfo.versionCode.toString()
        )
}