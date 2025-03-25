package com.markettwits.sportsouce.start.register.presentation.distances.component

import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance

interface StartDistancesComponent {

    val state: StartDistancesInput

    fun onClickDistance(distinctDistance: DistinctDistance)

    fun onClickGoBack()

}