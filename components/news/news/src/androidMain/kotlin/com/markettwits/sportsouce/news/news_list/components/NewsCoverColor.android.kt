package com.markettwits.sportsouce.news.news_list.components

import coil3.request.ImageRequest
import coil3.request.allowHardware

internal actual fun ImageRequest.Builder.enableNewsCoverSampling(): ImageRequest.Builder =
    allowHardware(false)
