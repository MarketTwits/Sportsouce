package com.markettwits.core.errors.impl.supcomponents

import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.supcomponents.icons.DatabaseNetwork
import com.markettwits.core.errors.impl.supcomponents.icons.NetworkOffline
import com.markettwits.core.errors.impl.supcomponents.icons.TablerError404

internal fun SauceError.mapSauceErrorImage() : ImageVector =
    when(this){
        is SauceError.Connection -> NetworkOffline
        is SauceError.Empty -> DatabaseNetwork
        is SauceError.General -> DatabaseNetwork
        is SauceError.JsonConverter -> DatabaseNetwork
        is SauceError.NotFound -> TablerError404
        is SauceError.WrongRequest -> DatabaseNetwork
    }