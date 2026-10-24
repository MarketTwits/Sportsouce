package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

@Composable
internal fun ProfileRegistrationsBlock(
    modifier: Modifier = Modifier, registrations: List<StartOrderInfo>,
    registrationsCount: Int = registrations.size,
    onClickRegistration: (StartOrderInfo) -> Unit, onClickViewAll: () -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.padding(horizontal = 16.dp)) {
            ProfileSectionHeader("Мои регистрации", if (registrationsCount == 0) "Все" else "Все $registrationsCount", onClickViewAll)
        }
        if (registrations.isEmpty()) {
            OnBackgroundCard(Modifier.padding(horizontal = 16.dp)) {
                Text("Регистраций пока нет", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary)
            }
        } else BoxWithConstraints(Modifier.fillMaxWidth()) {
            val cardWidth = (maxWidth - 32.dp).coerceAtLeast(0.dp).coerceAtMost(280.dp)
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                registrations.take(3).forEach { registration ->
                    ProfilePreviewCard(registration.startTitle, registration.image, registration.dateStartPreview,
                        modifier = Modifier.width(cardWidth), onClick = { onClickRegistration(registration) }) {
                        SauceStatusBadge(registration.payment.title, tone = registration.payment.profileTone())
                    }
                }
            }
        }
    }
}
