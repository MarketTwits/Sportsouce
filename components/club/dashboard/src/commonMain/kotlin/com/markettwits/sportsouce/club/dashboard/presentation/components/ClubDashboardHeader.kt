package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import org.jetbrains.compose.resources.painterResource
import sportsouce.components.club.dashboard.generated.resources.Res
import sportsouce.components.club.dashboard.generated.resources.im_club_main_image

@Composable
fun ClubDashboardHeader(imageUrl: String = "") {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // The saved website puts the photo first on mobile, with a single quiet caption.
        Box(Modifier.fillMaxWidth().aspectRatio(1.12f).clip(RoundedCornerShape(24.dp))) {
            // Keep the club's photo collage while loading and offline; this is the bundled website hero.
            Image(painterResource(Res.drawable.im_club_main_image), "Тренировки и команда СпортСоюза", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            if (imageUrl.isNotBlank()) SubcomposeAsyncImage(model = imageRequestCrossfade(imageUrl), contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop,
                loading = {}, error = {}, success = { SubcomposeAsyncImageContent() })
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(
                0f to Color.Transparent, .5f to Color.Transparent, 1f to Color.Black.copy(alpha = .58f))))
            Text("Движение объединяет", Modifier.align(Alignment.BottomStart).padding(20.dp),
                style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Спортивный клуб · Новосибирск", style = MaterialTheme.typography.labelMedium,
                color = SauceTheme.colors.action)
            Text("Двигаться вместе —\nинтереснее.", style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold, letterSpacing = (-1).sp), color = SauceTheme.colors.textPrimary)
            Text("Бег, плавание и лыжи в компании людей, которым нравится спорт. Тренировки, старты, поездки и всё, что случается между ними.",
                style = MaterialTheme.typography.bodyLarge, color = SauceTheme.colors.textSecondary)
        }
    }
}
