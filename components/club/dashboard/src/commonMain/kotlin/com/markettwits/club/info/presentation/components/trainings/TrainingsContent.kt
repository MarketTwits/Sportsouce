package com.markettwits.club.info.presentation.components.trainings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.club.info.domain.models.Training
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun TrainingsContent(
    modifier: Modifier = Modifier,
    trainings: List<Training>
) {
    Column(modifier = modifier.padding(10.dp)) {
        Text(
            textAlign = TextAlign.Start,
            text = "Тренировки, которые выбирают",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        TrainingsContents(
            modifier = Modifier.padding(10.dp),
            trainings = trainings
        )
    }
}

@Composable
fun TrainingsContents(
    modifier: Modifier = Modifier,
    trainings: List<Training>
) {
    LazyColumn(modifier = modifier) {
        items(trainings) { training ->
            TrainingItemContent(
                training = training
            )
        }
    }
}

@Composable
fun TrainingItemContent(
    modifier: Modifier = Modifier,
    training: Training
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(Shapes.medium),
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