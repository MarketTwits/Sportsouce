package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.booleanOrNull

@Serializable
data class RegistrationCandidate(
    val id: Int,
    val name: String? = null,
    val surname: String? = null,
    val team: String? = null,
    val distance: String? = null,
    val start: CandidateStart? = null,
) {
    @Serializable
    data class CandidateStart(
        val name: String? = null,
        @SerialName("start_date") val date: String? = null,
        @SerialName("start_time") val time: String? = null,
    )
}

@Serializable
data class ExternalRegistration(
    val id: Int,
    val startName: String,
    val distanceName: String,
    val startDate: String,
    val url: String,
    val ageGroup: String? = null,
    val team: String? = null,
    val posterId: Int? = null,
    val poster: Poster? = null,
) {
    @Serializable
    data class Poster(val id: Int? = null, val fullPath: String? = null, val path: String? = null, val name: String? = null) {
        val url: String? get() = fullPath ?: path?.let { directory -> name?.let { name -> "${directory.trimEnd('/')}/$name" } }
    }
}

@Serializable
data class ExternalRegistrationPage(val count: Int? = null, val rows: List<ExternalRegistration>)

@Serializable
data class RegistrationCorrection(
    val memberStartId: Int,
    val type: String,
    val name: String,
    val surname: String,
    val reason: String,
    val birthYear: Int? = null,
)

@Serializable
data class RegistrationTransferOptions(
    val feeKopecks: Int = 0,
    val expiresAt: String? = null,
    val blocked: String? = null,
    val options: List<Option> = emptyList(),
) {
    @Serializable
    data class Option(
        val answerId: Int,
        val name: String,
        val variant: String? = null,
        val priceKopecks: Int = 0,
        val goesWithSlot: Boolean = false,
    )
}

@Serializable
data class RegistrationTransferRecipient(
    val found: Boolean = false,
    val canInvite: Boolean = false,
    val inviteEmail: String? = null,
    val recipient: Recipient? = null,
    val refused: String? = null,
    val checkedOnAnswer: Boolean = false,
) {
    @Serializable
    data class Recipient(val id: Int, val maskedName: String)
}

@Serializable
data class RegistrationTransfer(
    val mode: String,
    val options: List<Option>,
    val targetUserId: Int? = null,
    val inviteEmail: String? = null,
) {
    @Serializable
    data class Option(val answerId: Int, val action: String)
}

@Serializable
data class RegistrationRefundRules(
    val source: String? = null,
    @SerialName("start_date") val startDate: String? = null,
    val rules: List<Rule> = emptyList(),
) {
    @Serializable
    data class Rule(
        @SerialName("refund_percent") val refundPercent: Int,
        @SerialName("days_before_start") val daysBeforeStart: Int,
        val description: String? = null,
    )
}

@Serializable
data class RegistrationOptions(
    val options: List<RegistrationOption> = emptyList(),
    val paidOptions: List<RegistrationOption> = emptyList(),
    val freeAnswers: List<FreeAnswer> = emptyList(),
    val totalPrice: Int? = null,
) {
    @Serializable
    data class FreeAnswer(
        val additionalFieldId: Int,
        val memberStartId: Int,
        val answerId: Int,
        val name: String,
        val displayValue: String? = null,
    )
}

@Serializable
data class RegistrationOption(
    val additionalFieldId: Int,
    val optionId: Int? = null,
    val distanceId: Int,
    val memberIndex: Int,
    val memberStartId: Int,
    val answerId: Int? = null,
    val name: String,
    val price: Int? = null,
    val status: String,
    val type: String,
    val field: Field,
    val currentValue: JsonObject? = null,
    val displayValue: String? = null,
    val pendingChangeRequest: ChangeRequest? = null,
) {
    val key: String get() = "$memberStartId:$additionalFieldId:$optionId:$status"
    val needsAnswer: Boolean get() = status == "available" && (optionId == null || type != "Мультиселект")
    val answerKey: String get() = when (type) {
        "Дата" -> "date"
        "Число" -> "number"
        "Чекбокс" -> "bool"
        "Синглселект" -> "singleSelect"
        "Мультиселект" -> "multiSelect"
        else -> "string"
    }

    @Serializable
    data class Field(
        val name: String,
        val description: String? = null,
        val images: JsonElement? = null,
        val options: List<Choice> = emptyList(),
    )

    @Serializable
    data class Choice(
        val id: Int,
        val title: String,
        val price: Int? = null,
        val description: String? = null,
        val images: JsonElement? = null,
        @SerialName("stock_left") val stockLeft: Int? = null,
    )

    @Serializable
    data class ChangeRequest(
        val id: Int,
        val status: String,
        @SerialName("display_new_value") val displayNewValue: String? = null,
        @SerialName("rejection_reason") val rejectionReason: String? = null,
        @SerialName("surcharge_amount") val surchargeAmount: Int? = null,
    )
}

@Serializable
data class RegistrationMutation(
    val success: Boolean? = null,
    val payment: JsonElement? = null,
    val message: JsonElement? = null,
    val formUrl: String? = null,
) {
    val completedWithoutRedirect: Boolean get() = (payment as? JsonPrimitive)?.booleanOrNull == true

    val paymentUrl: String? get() = (formUrl ?: when (val value = payment) {
        is JsonObject -> (value["formUrl"] as? JsonPrimitive)?.contentOrNull
        is JsonPrimitive -> value.takeIf { it.isString }?.contentOrNull
        else -> null
    })?.takeIf { it.startsWith("https://", ignoreCase = true) || it.startsWith("http://", ignoreCase = true) }
}

@Serializable
data class OutgoingRegistrationTransfer(
    val id: Int,
    val status: String,
    val isTransfer: Boolean = false,
    val memberStartId: Int? = null,
    val expiresAt: String? = null,
    val feeKopecks: Int? = null,
    val keptOptions: List<KeptOption> = emptyList(),
) {
    @Serializable
    data class KeptOption(val name: String, val variant: String? = null)
}

@Serializable
internal data class OutgoingRegistrationTransfers(val rows: List<OutgoingRegistrationTransfer>)

@Serializable
data class RegistrationPriceQuote(
    val priceWithoutDiscount: Int = 0,
    val additionalFieldsPrice: Int = 0,
    val totalPrice: Int = 0,
    val isPaymentRequired: Boolean = true,
    val isPersonalDiscountApplied: Boolean = false,
    val personalDiscountPercent: Double? = null,
    val reRegistrationFullPrice: RegistrationPriceQuote? = null,
)
