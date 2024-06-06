package com.markettwits.start.register.presentation.order.domain.price


import com.markettwits.start.register.presentation.order.domain.OrderStatement


internal fun OrderStatement.updatePriceWithDiscount(): OrderStatement {
    return this.calculateAgeDiscount().calculatePromoDiscount().calculateComboDiscount()
}

private fun OrderStatement.calculateAgeDiscount(): OrderStatement {
    val totalMembers = distanceInfo.flatMap { it.members }.size
    val pricePerMember = (orderPrice.initialTotal - orderPrice.discountComboInCache) / totalMembers
    val totalDiscounts = mutableListOf(0)
    distanceInfo.flatMap { distance ->
        distance.members.map { member ->
            val age = member.age.toIntOrNull() ?: 0 // Парсим возраст, если не удалось, используем 0
            val applicableDiscount = discounts.find { discount ->
                age in discount.c_to..discount.c_from || age in discount.c_from..discount.c_to
            }
            val discountedPrice = if (applicableDiscount != null) {
                val discountValue = if (applicableDiscount.percent) {
                    pricePerMember * applicableDiscount.value / 100
                } else {
                    applicableDiscount.value
                }
                totalDiscounts.add(discountValue)
            } else {
                pricePerMember
            }
            discountedPrice
        }
    }
    val totalPrice = orderPrice.initialTotal - totalDiscounts.sum()
    val orderPrice = orderPrice.copy(total = totalPrice, discountAgeInCache = totalDiscounts.sum())
    return this.copy(orderPrice = orderPrice)
}


private fun OrderStatement.calculatePromoDiscount(): OrderStatement {
    return if (this.orderPrice.discountPromoInCache != this.orderPrice.promoDiscountInPercent) {
        val discountPrice = orderPrice.total / orderPrice.promoDiscountInPercent
        val total = this.orderPrice.total - discountPrice
        val orderPrice = this.orderPrice.copy(total = total, discountPromoInCache = discountPrice)
        return this.copy(orderPrice = orderPrice)
    } else {
        this
    }
}

private fun OrderStatement.calculateComboDiscount(): OrderStatement {
    return if (this.orderPrice.discountComboInCache != 0) {
        val total = this.orderPrice.total - this.orderPrice.discountComboInCache
        val orderPrice = this.orderPrice.copy(total = total)
        return this.copy(orderPrice = orderPrice)
    } else {
        this
    }
}