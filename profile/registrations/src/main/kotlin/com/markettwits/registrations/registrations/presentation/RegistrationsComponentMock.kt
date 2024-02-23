package com.markettwits.registrations.registrations.presentation

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations.domain.StartPaymentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationsComponentMock : RegistrationsComponent {

    override val value: StateFlow<RegistrationsStore.State> = MutableStateFlow(
        RegistrationsStore.State(
            info = testList,
            paymentState = StartPaymentState(
                paymentList = testPaymant
            )
        )
    )

    override fun obtainEvent(event: RegistrationsStore.Intent) {

    }

    companion object {
        private val testPaymant =
            listOf(
                StartOrderInfo(
                    id = 1,
                    name = "Sample Event",
                    image = "sample_image.jpg",
                    dateStart = "2022-01-01",
                    statusCode = StartStatus(2, "Регистрация открыта"),
                    team = "Sample Team",
                    payment = false,
                    ageGroup = "18-30",
                    distance = "5K",
                    member = "John Doe",
                    kindOfSport = "Running",
                    startTitle = "Sample Title",
                    startId = 0,
                    cost = "500"
                )
            )
        private val testList = listOf(
            StartOrderInfo(
                id = 1,
                name = "Sample Event",
                image = "sample_image.jpg",
                dateStart = "2022-01-01",
                statusCode = StartStatus(2, "Регистрация открыта"),
                team = "Sample Team",
                payment = false,
                ageGroup = "18-30",
                distance = "5K",
                member = "John Doe",
                kindOfSport = "Running",
                startTitle = "Sample Title",
                startId = 0,
                cost = "500"
            ),
            StartOrderInfo(
                id = 1,
                name = "Sample Event",
                image = "sample_image.jpg",
                dateStart = "2022-01-01",
                statusCode = StartStatus(2, "Регистрация открыта"),
                team = "Sample Team",
                payment = true,
                ageGroup = "18-30",
                distance = "5K",
                member = "John Doe",
                kindOfSport = "Running",
                startTitle = "Sample Title",
                startId = 0,
                cost = "500"
            ),
        )
    }

}