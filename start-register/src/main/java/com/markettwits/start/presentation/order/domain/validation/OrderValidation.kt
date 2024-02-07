package com.markettwits.start.presentation.order.domain.validation

import com.markettwits.start.presentation.order.domain.OrderStatement

interface OrderValidation {
    fun validate(orderStatement: OrderStatement): Result<OrderStatement>
}