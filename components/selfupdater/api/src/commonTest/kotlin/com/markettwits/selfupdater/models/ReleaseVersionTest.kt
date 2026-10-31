package com.markettwits.selfupdater.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReleaseVersionTest {
    @Test
    fun parsesPublishedAndPrefixedTags() {
        assertEquals(ReleaseVersion("1.6.1", 10422), ReleaseVersion.fromTag("1.6.1.10422"))
        assertEquals(ReleaseVersion("1.10.0", 10423), ReleaseVersion.fromTag("v1.10.0.10423"))
    }

    @Test
    fun rejectsIncompleteOrAmbiguousTags() {
        listOf("1.6.1", "1.6.1.0", "1.6.1.-1", "1.6.1.10422-debug", "1.6.1.10422.1", "1..1.2", " 1.6.1.1", "1.6.1.999999999999999999999999")
            .forEach { assertNull(ReleaseVersion.fromTag(it), it) }
    }
}
