//package com.markettwits.sportsouce.club.example
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.kabu.bneibaruchseekers.core.model.data.journey.Journey
//import com.kabu.bneibaruchseekers.core.ui.components.journey.JourneyOrderView
//import com.kabu.bneibaruchseekers.resources.Res
//import com.kabu.bneibaruchseekers.resources.ic_arrow_back
//import com.kabu.bneibaruchseekers.resources.my_journey
//import org.jetbrains.compose.resources.stringResource
//import org.jetbrains.compose.resources.vectorResource
//
//@Composable
//internal fun JourneyDetailTopBar(
//    modifier: Modifier = Modifier,
//    journey: Journey,
//    onBackClick: () -> Unit,
//) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Box(
//            modifier = Modifier
//                .height(48.dp)
//                .fillMaxWidth()
//                .padding(horizontal = 12.dp),
//        ) {
//            IconButton(
//                modifier = Modifier
//                    .align(Alignment.CenterStart),
//                onClick = onBackClick,
//            ) {
//                Icon(
//                    imageVector = vectorResource(Res.drawable.ic_arrow_back),
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary,
//                )
//            }
//
//            Text(
//                modifier = Modifier.align(Alignment.Center),
//                text = stringResource(Res.string.my_journey),
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.secondary,
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 38.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            JourneyOrderView(order = journey.order)
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                text = journey.title,
//                style = MaterialTheme.typography.headlineMedium,
//                textAlign = TextAlign.Center,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 2,
//            )
//        }
//
//        Spacer(Modifier.height(51.dp))
//
//        /**
//         * Map progress
//         */
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 24.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            CustomLinearProgressIndicator(
//                modifier = Modifier
//                    .weight(1F)
//                    .height(8.dp),
//                progress = journey.progress,
//                progressColor = MaterialTheme.colorScheme.tertiary,
//                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                clipShape = RoundedCornerShape(20.dp),
//            )
//
//            Spacer(Modifier.width(12.dp))
//
//            Text(
//                text = buildString {
//                    append(journey.completedWisdomLessons.size + journey.completedDailyDoseLesson.size)
//                    append("/")
//                    append(journey.lessons.size)
//                },
//                style = MaterialTheme.typography.titleSmall,
//                fontSize = 16.sp,
//                maxLines = 1,
//            )
//        }
//
//        Spacer(Modifier.height(12.dp))
//    }
//}
//
//@Composable
//private fun CustomLinearProgressIndicator(
//    modifier: Modifier = Modifier,
//    progress: Float,
//    progressColor: Color,
//    backgroundColor: Color,
//    clipShape: Shape = RoundedCornerShape(16.dp)
//) {
//    Box(
//        modifier = modifier
//            .clip(clipShape)
//            .background(backgroundColor)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth(progress)
//                .clip(clipShape)
//                .background(progressColor)
//        )
//    }
//}