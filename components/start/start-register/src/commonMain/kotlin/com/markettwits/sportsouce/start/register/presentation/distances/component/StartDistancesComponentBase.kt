package com.markettwits.sportsouce.start.register.presentation.distances.component

import com.arkivanov.decompose.ComponentContext
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartRegistrationInput

class StartDistancesComponentBase(
    componentContext: ComponentContext,
    private val input: StartDistancesInput,
    private val output: StartDistancesOutput
) : ComponentContext by componentContext, StartDistancesComponent {

    override val state: StartDistancesInput = input

    override fun onClickDistance(distinctDistance: DistinctDistance) {
        output.onClickDistance(getSelectedDistance(distinctDistance))
    }

    override fun onClickGoBack() {
        output.onClickGoBack()
    }

    private fun getSelectedDistance(
        distinctDistance: DistinctDistance
    ): StartRegistrationInput {
        input.let {
            val matchingDistance = it.mapDistance.find { it.id == distinctDistance.id }
            val comboId = if (matchingDistance?.combo.isNullOrEmpty()) null else matchingDistance?.id
            val distances = if (matchingDistance?.combo != null) {
                it.distance.filter { it.id in matchingDistance.combo!! }
            } else {
                listOf(distinctDistance)
            }
            return StartRegistrationInput(
                startId = it.startId,
                paymentType = it.paymentType,
                comboId = comboId,
                startTitle = input.startTitle,
                distances = distances,
                isPaymentDisabled = input.paymentDisabled
            )
        }
    }


}