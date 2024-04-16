package com.markettwits.registrations.list.presentation.component

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.list.presentation.store.RegistrationsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationsComponentMock : RegistrationsComponent {

    override val value: StateFlow<RegistrationsStore.State> = MutableStateFlow(
        RegistrationsStore.State(
            base = testList,
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
                    dateStartPreview = "2022-01-01",
                    dateStartCloud = "",
                    statusCode = StartStatus(2, "Регистрация открыта"),
                    team = "Sample Team",
                    payment = StartOrderInfo.PaymentStatus.Failure(),
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
                dateStartPreview = "2022-01-01",
                statusCode = StartStatus(2, "Регистрация открыта"),
                team = "Sample Team",
                payment = StartOrderInfo.PaymentStatus.Failure(),
                ageGroup = "18-30",
                distance = "5K",
                member = "John Doe",
                kindOfSport = "Running",
                startTitle = "Sample Title",
                dateStartCloud = "",
                startId = 0,
                cost = "500"
            ),
            StartOrderInfo(
                id = 1,
                name = "Sample Event",
                image = "sample_image.jpg",
                dateStartPreview = "2022-01-01",
                statusCode = StartStatus(2, "Регистрация открыта"),
                team = "Sample Team",
                payment = StartOrderInfo.PaymentStatus.Failure(),
                ageGroup = "18-30",
                distance = "5K",
                member = "John Doe",
                kindOfSport = "Running",
                startTitle = "Sample Title",
                dateStartCloud = "",
                startId = 0,
                cost = "500"
            ),
        )
    }

}