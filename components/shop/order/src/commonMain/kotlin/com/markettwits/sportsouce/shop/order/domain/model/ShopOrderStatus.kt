package com.markettwits.sportsouce.shop.order.domain.model

sealed interface ShopOrderStatus {

    /**
     * The order is confirmed and ready for processing.
     */
    data object Confirmed : ShopOrderStatus {
        override fun toString(): String  = "Подтвержден"
    }

    /**
     * The order has been fully completed.
     */
    data object Completed : ShopOrderStatus {
        override fun toString(): String = "Завершён"
    }

    /**
     * The order is currently pending.
     */
    data object Pending : ShopOrderStatus {
        override fun toString(): String = "В ожидании"
    }

    /**
     * The order has been canceled by an administrator.
     */
    data object Cancelled : ShopOrderStatus {
        override fun toString(): String  = "Отменен"
    }

    /**
     * The order is unpaid and has no status.
     */
    data object Empty : ShopOrderStatus {
        override fun toString(): String = "Без статуса"
    }
}