package com.markettwits.start.register.presentation.distances.component

import com.markettwits.start_cloud.model.start.fields.DistinctDistance

interface StartDistancesComponent {

    val state: StartDistancesInput

    fun onClickDistance(distinctDistance: DistinctDistance)

    fun onClickGoBack()

}