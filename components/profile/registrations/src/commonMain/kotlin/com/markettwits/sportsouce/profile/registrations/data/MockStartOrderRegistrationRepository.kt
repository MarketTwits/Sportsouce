package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Temporary mock implementation for StartOrderRegistrationRepository
 * Used when the real service is unavailable
 * Includes 3-second loading delay as requested
 */
class MockStartOrderRegistrationRepository : StartOrderRegistrationRepository {

    override fun registrations(): Flow<List<StartOrderInfo>> = flow {
        // 3-second delay as requested
        delay(3000)

        emit(createMockRegistrations())
    }

    override suspend fun currentUser(): Result<SharedUser> {
        delay(1000) // Shorter delay for user info
        return Result.success(createMockUser())
    }

    override suspend fun pay(id: Int): Result<String> {
        delay(2000)
        return Result.success("mock_payment_url")
    }

    override suspend fun getPrice(
        orderId: Int,
        orderDistancesId: List<String>,
        startId: Int,
    ): Result<StartOrderPrice> {
        delay(1500)
        return Result.success(
            StartOrderPrice(
                additionalPrice = "500",
                totalPrice = "1500",
                isRequired = true
            )
        )
    }

    private fun createMockRegistrations(): List<StartOrderInfo> {
        return listOf(
            StartOrderInfo(
                id = 1,
                startId = 101,
                name = "Весенний марафон 2024",
                image = "https://example.com/marathon1.jpg",
                dateStartPreview = "15 мая 2024",
                dateStartCloud = "2024-05-15T09:00:00Z",
                promo = "SPRING2024",
                payment = StartOrderPaymentStatus.Success(),
                members = listOf(
                    StartOrderMember(
                        name = "Иван",
                        surname = "Петров",
                        teamName = "Команда Спорт",
                        ageGroupName = "18-35",
                        distanceName = "10 км",
                        genderName = "Мужской",
                        formatName = "Индивидуальный",
                        results = emptyList()
                    )
                ),
                startTitle = "Весенний марафон 2024",
                cost = "1500"
            ),
            StartOrderInfo(
                id = 2,
                startId = 102,
                name = "Летний триатлон",
                image = "https://example.com/triathlon1.jpg",
                dateStartPreview = "20 июня 2024",
                dateStartCloud = "2024-06-20T07:00:00Z",
                promo = "",
                payment = StartOrderPaymentStatus.NotPaid(),
                members = listOf(
                    StartOrderMember(
                        name = "Мария",
                        surname = "Иванова",
                        teamName = "Триатлон Клуб",
                        ageGroupName = "25-40",
                        distanceName = "Спринт",
                        genderName = "Женский",
                        formatName = "Индивидуальный",
                        results = emptyList()
                    )
                ),
                startTitle = "Летний триатлон",
                cost = "2500"
            ),
            StartOrderInfo(
                id = 3,
                startId = 103,
                name = "Благотворительный забег",
                image = "https://example.com/charity1.jpg",
                dateStartPreview = "1 июля 2024",
                dateStartCloud = "2024-07-01T08:00:00Z",
                promo = "CHARITY",
                payment = StartOrderPaymentStatus.Free(),
                members = listOf(
                    StartOrderMember(
                        name = "Алексей",
                        surname = "Сидоров",
                        teamName = "Волонтеры",
                        ageGroupName = "16+",
                        distanceName = "5 км",
                        genderName = "Мужской",
                        formatName = "Командный",
                        results = listOf(
                            StartOrderMemberResult(
                                bodyNumber = "123",
                                circles = mapOf(1 to "25:30"),
                                distance = "5 км",
                                id = 1,
                                memberStartId = 101,
                                userName = "Алексей Сидоров",
                                place = 15,
                                result = "25:30",
                                sex = "Мужской",
                                shift = "1",
                                team = "Волонтеры"
                            )
                        )
                    )
                ),
                startTitle = "Благотворительный забег",
                cost = "0"
            ),
            StartOrderInfo(
                id = 4,
                startId = 104,
                name = "Городской велокросс",
                image = "https://example.com/bike1.jpg",
                dateStartPreview = "10 августа 2024",
                dateStartCloud = "2024-08-10T10:00:00Z",
                promo = "",
                payment = StartOrderPaymentStatus.OnPlace(),
                members = listOf(
                    StartOrderMember(
                        name = "Екатерина",
                        surname = "Смирнова",
                        teamName = "Велоклуб Старт",
                        ageGroupName = "20-35",
                        distanceName = "30 км",
                        genderName = "Женский",
                        formatName = "Индивидуальный",
                        results = emptyList()
                    )
                ),
                startTitle = "Городской велокросс",
                cost = "1200"
            ),
            StartOrderInfo(
                id = 5,
                startId = 105,
                name = "Зимний лыжный марафон",
                image = "https://example.com/ski1.jpg",
                dateStartPreview = "25 декабря 2024",
                dateStartCloud = "2024-12-25T11:00:00Z",
                promo = "WINTER25",
                payment = StartOrderPaymentStatus.PaymentCancelled(),
                members = listOf(
                    StartOrderMember(
                        name = "Дмитрий",
                        surname = "Козлов",
                        teamName = "Лыжники",
                        ageGroupName = "30-45",
                        distanceName = "15 км",
                        genderName = "Мужской",
                        formatName = "Индивидуальный",
                        results = emptyList()
                    )
                ),
                startTitle = "Зимний лыжный марафон",
                cost = "1800"
            )
        )
    }

    private fun createMockUser(): SharedUser {
        return SharedUser(
            id = 12345,
            age = "30",
            name = "Тест",
            surname = "Пользователь",
            phone = "+7900000000",
            imageUrl = "https://example.com/avatar.jpg"
        )
    }
}