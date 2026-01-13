package com.markettwits.activityholder

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Bundle
import java.lang.ref.WeakReference

object CurrentActivityHolder : Application.ActivityLifecycleCallbacks {

    private var currentActivity = WeakReference<Activity>(null)
    private var isDebuggableFlag: Boolean = false

    fun register(application: Application) {
        isDebuggableFlag =
            (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        application.registerActivityLifecycleCallbacks(this)
    }

    fun getCurrentActivity(): Activity? = currentActivity.get()

    fun isDebuggable(): Boolean {
        return isDebuggableFlag
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    // Unused fun
    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit
}
