package com.markettwits.sportsouce.club.info.presentation.components.trainings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.club.info.domain.models.Training

@Composable
fun TrainingsContent(
    modifier: Modifier = Modifier,
    trainings: List<Training>
) {
    var selectedImageUrl by rememberSaveable {
        mutableStateOf("")
    }

    if (selectedImageUrl.isNotEmpty()) {
        FullImageScreen(image = selectedImageUrl, onDismiss = { selectedImageUrl = "" })
    }

    LazyColumn(modifier = modifier) {
        items(trainings) { training ->
            TrainingItemContent(
                modifier = Modifier.padding(8.dp),
                training = training,
            ) {
                selectedImageUrl = training.imageUrl
            }
        }
    }
}

@Composable
fun TrainingItemContent(
    modifier: Modifier = Modifier,
    training: Training,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(Shapes.medium)
                .clickable(onClick = onClick),
            model = imageRequestCrossfade(training.imageUrl),
            contentDescription = "image",
            contentScale = ContentScale.FillBounds
        )
        Column(modifier = modifier.padding(10.dp)) {
            Text(
                textAlign = TextAlign.Start,
                text = training.type,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            HtmlText(
                textAlign = TextAlign.Start,
                text = training.htmlDescription,
                fontSize = 12.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}