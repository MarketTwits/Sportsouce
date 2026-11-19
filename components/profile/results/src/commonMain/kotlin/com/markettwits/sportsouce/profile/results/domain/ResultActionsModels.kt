package com.markettwits.sportsouce.profile.results.domain

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlin.math.round

@Serializable
data class NamedEntity(val id: Int? = null, val name: String? = null)
@Serializable
data class StartSuggestion(val id: Int, val name: String = "", val start_date: String? = null)
@Serializable
data class StartSuggestions(val suggestions: List<StartSuggestion> = emptyList())
@Serializable
data class UnmatchedResults(val rows: List<UnmatchedResult> = emptyList())
@Serializable
data class UnmatchedResult(val id: Int, val name: String = "", val birthday: String? = null, val birthYear: Int? = null,
    val birthYearAnomaly: Boolean = false, val result: String? = null, val distance: String? = null, val start: NamedEntity? = null)
@Serializable
data class ResultClaims(val rows: List<ResultClaim> = emptyList())
@Serializable
data class ResultClaim(val id: Int, val type: String = "", val status: String = "", val comment: String? = null,
    val title: String? = null, val result: ClaimedResult? = null, val createdAt: String? = null, val requester: NamedEntity? = null)
@Serializable
data class ClaimedResult(val id: Int? = null, val name: String? = null, val start: NamedEntity? = null)
@Serializable
data class ResultCandidates(val rows: List<ResultCandidate> = emptyList())
@Serializable
data class ResultCandidate(val id: Int, val status: String? = null, val targetType: String? = null,
    val displayScore: Double? = null, val reasons: List<MatchReason> = emptyList(), val targetResult: CandidateResult? = null,
    val targetUser: CandidateUser? = null, val baseAgeGroupDisclaimer: String? = null)
@Serializable
data class CandidateUser(val id: Int? = null, val name: String? = null, val surname: String? = null, val birthday: String? = null, val isGrey: Boolean? = null)
@Serializable
data class CandidateResult(val id: Int? = null, val name: String? = null, val birthday: String? = null, val start: NamedEntity? = null, val result: String? = null, val distance: String? = null)
@Serializable
data class MatchReason(val code: String? = null, val label: String? = null, val positive: Boolean? = null)

@Serializable
data class ExternalResultRequest(val title: String, val dateText: String, val eventDate: String, val distance: String,
    val sport: String? = null, val distanceM: Double? = null, val resultText: String,
    val proofUrl: String? = null, val proofFileId: Int? = null, val description: String = "Результат добавлен в «Моих результатах»")

enum class ResultField { Title, Date, Category, Distance, Time, Proof }

data class ExternalResultDraft(val title: String = "", val date: String = "", val category: String = "other",
    val distance: String = "", val time: String = "", val proof: String = "") {
    fun update(field: ResultField, value: String) = when (field) {
        ResultField.Title -> copy(title = value); ResultField.Date -> copy(date = value)
        ResultField.Category -> copy(category = value); ResultField.Distance -> copy(distance = value)
        ResultField.Time -> copy(time = value); ResultField.Proof -> copy(proof = value)
    }
    fun errors(hasFile: Boolean): Map<ResultField, String> = buildMap {
        if (title.isBlank()) put(ResultField.Title, "Укажите название старта")
        val parsed = runCatching { LocalDate.parse(date) }.getOrNull()
        if (parsed == null) put(ResultField.Date, "Выберите дату старта")
        if (category.isBlank()) put(ResultField.Category, "Выберите дистанцию")
        val km = distance.replace(',', '.').toDoubleOrNull()
        if (category == "other" && (km == null || !km.isFinite() || km <= 0 || km > 10000)) put(ResultField.Distance, "Укажите дистанцию в километрах")
        if (!validResultTime(time)) put(ResultField.Time, "Укажите время: мм:сс или ч:мм:сс, например 1:42:18")
        if (proof.isBlank() && !hasFile) put(ResultField.Proof, "Добавьте ссылку на официальный результат или протокол")
        else if (proof.isNotBlank() && !Regex("https?://[^\\s/]+\\.[^\\s]+").matches(proof.trim())) put(ResultField.Proof, "Укажите полную ссылку http:// или https://")
    }
    fun request(categories: List<ResultCategory>, fileId: Int?): ExternalResultRequest {
        val custom = category == "other"
        val sport = if (custom) "run" else category.substringBefore(':')
        val meters = if (custom) round(distance.replace(',', '.').toDouble() * 1000) else category.substringAfter(':').toDoubleOrNull()
        val label = if (custom) "${distance.replace('.', ',')} км" else categories.find { it.key == category }?.label.orEmpty()
        return ExternalResultRequest(title.trim(), date.split('-').reversed().joinToString("."), date, label,
            sport, meters, time.trim().replace(',', '.'), proof.trim().takeIf { it.isNotEmpty() }, fileId)
    }
}

internal fun validResultTime(value: String): Boolean {
    val text = value.trim().replace(',', '.')
    if (!Regex("\\d{1,2}:\\d{2}(:\\d{2})?([.]\\d{1,3})?").matches(text)) return false
    val parts = text.substringBefore('.').split(':').map { it.toInt() }
    if (parts.drop(1).any { it >= 60 }) return false
    return parts.any { it > 0 } || (text.substringAfter('.', "0").toIntOrNull() ?: 0) > 0
}

class ResultProofFile(val name: String, val mimeType: String, val bytes: ByteArray, val lastModified: Long)
internal const val MAX_PROOF_BYTES = 10 * 1024 * 1024
internal fun ResultProofFile.validationError(): String? = when {
    bytes.isEmpty() -> "Файл пуст"
    bytes.size > MAX_PROOF_BYTES -> "Файл больше 10 МБ"
    mimeType != "application/pdf" && !mimeType.startsWith("image/") -> "Подойдёт PDF или изображение"
    else -> null
}
