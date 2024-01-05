package com.markettwits.registrations.registrations.presentation

import com.markettwits.cloud.model.common.StartStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationsComponentMock : RegistrationsComponent {

    override val value: StateFlow<RegistrationsStore.State> = MutableStateFlow(
        RegistrationsStore.State(
            info = testList,
            paymentState = RegistrationsStore.StartPaymentState(
                paymentList = testPaymant
            )
        )
    )

    override fun obtainEvent(event: RegistrationsStore.Intent) {

    }

    companion object {
        private val testPaymant =
            listOf(
                RegistrationsStore.StartsStateInfo(
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
            RegistrationsStore.StartsStateInfo(
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
            RegistrationsStore.StartsStateInfo(
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