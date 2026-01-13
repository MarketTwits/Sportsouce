package com.markettwits.buildkonfig

import com.markettwits.activityholder.CurrentActivityHolder

actual val isDebugMode: Boolean
    get() = CurrentActivityHolder.isDebuggable()
