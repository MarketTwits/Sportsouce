package com.markettwits.selfupdater.thirdparty.api

import kotlinx.serialization.Serializable
import com.markettwits.selfupdater.models.ReleaseNotesLocale

/** Describes a successfully deployed browser build, independently of GitHub app releases. */
@Serializable
data class WebDeployment(
    val schemaVersion: Int,
    val sequence: Long,
    val versionName: String,
    val buildNumber: Long,
    val changes: Map<String, String> = emptyMap(),
) {
    fun isNewerThan(currentSequence: Long, currentBuild: Long): Boolean {
        require(schemaVersion == 1 && sequence > 0 && buildNumber > 0 && versionName.isNotBlank()) {
            "Invalid web deployment metadata"
        }
        require(currentSequence > 0 && currentBuild > 0) { "Current web build metadata is unavailable" }
        return sequence > currentSequence && buildNumber >= currentBuild
    }

    fun description(language: String = ReleaseNotesLocale.Default): String {
        val normalized = language.replace('_', '-').lowercase()
        val entries = changes.entries.associate { it.key.replace('_', '-').lowercase() to it.value }
        fun nonBlank(key: String) = entries[key]?.takeIf { it.isNotBlank() }
        val localized = nonBlank(normalized) ?: nonBlank(normalized.substringBefore('-'))
        return localized ?: if (ReleaseNotesLocale.isRussian(language))
            nonBlank("ru-ru") ?: ReleaseNotesLocale.unavailable(language)
        else nonBlank("en") ?: nonBlank("en-us") ?: ReleaseNotesLocale.unavailable(language)
    }
}
