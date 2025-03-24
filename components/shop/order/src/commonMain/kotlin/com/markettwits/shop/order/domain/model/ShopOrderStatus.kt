package com.markettwits.shop.order.domain.model

sealed interface ShopOrderStatus {

    data object Confirmed : ShopOrderStatus{
        override fun toString(): String  = "Подтвержден"
    }

    data object Completed : ShopOrderStatus{
        override fun toString(): String = "Завершён"
    }

    data object Pending : ShopOrderStatus{
        override fun toString(): String = "В ожидании"
    }

    data object Cancelled : ShopOrderStatus{
        override fun toString(): String  = "Отменен"
    }

    data object Empty : ShopOrderStatus{
        override fun toString(): String = "Без статуса"
    }
}