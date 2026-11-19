package com.markettwits.core_ui.items.image

import coil3.request.ImageRequest
import coil3.request.allowHardware

actual fun ImageRequest.Builder.enableImageColorSampling(): ImageRequest.Builder =
    allowHardware(false)
