package com.markettwits.sportsouce.profile.members

import com.markettwits.sportsouce.profile.cloud.model.identity.IdentityPreviewCandidate
import com.markettwits.sportsouce.profile.cloud.model.identity.IdentityPreviewUser
import com.markettwits.sportsouce.profile.members.member_common.data.toMemberCandidate
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.*

class IdentityCandidateMappingTest {
    @Test fun candidateUsesCanonicalIdentityReturnedByTheServer() {
        val candidate = IdentityPreviewCandidate(IdentityPreviewUser(1282, "Константин ", "Маняшин", 2004,
            city = JsonPrimitive("Новосибирск")), confidenceLevel = "MEDIUM")
        val mapped = candidate.toMemberCandidate(currentUserId = 2310)
        assertEquals("Константин", mapped.name)
        assertEquals("Маняшин", mapped.surname)
        assertEquals(2004, mapped.birthYear)
        assertEquals("Новосибирск", mapped.city)
        assertTrue(mapped.canRequestConnection)
    }

    @Test fun missingProfileDataIsNotReplacedBySearchValues() {
        val mapped = IdentityPreviewCandidate(IdentityPreviewUser(1282)).toMemberCandidate(2310)
        assertEquals("", mapped.name); assertEquals("", mapped.surname); assertNull(mapped.birthYear)
    }

    @Test fun ownAndGreyProfilesRemainUnavailableForConnectionRequests() {
        val own = IdentityPreviewCandidate(IdentityPreviewUser(2310)).toMemberCandidate(2310)
        val grey = IdentityPreviewCandidate(IdentityPreviewUser(1282, isGrey = true)).toMemberCandidate(2310)
        assertFalse(own.canRequestConnection); assertFalse(grey.canRequestConnection)
        assertEquals("Это ваш профиль", own.unavailableReason)
        assertEquals("Профиль без аккаунта", grey.unavailableReason)
    }
}
