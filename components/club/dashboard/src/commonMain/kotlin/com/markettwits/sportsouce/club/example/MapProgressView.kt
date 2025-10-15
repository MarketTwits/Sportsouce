//package com.kabu.bneibaruchseekers.core.ui.components.journey
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.kabu.bneibaruchseekers.core.model.data.journey.Journey
//import com.kabu.bneibaruchseekers.core.model.data.journey.getCompletedJourneyList
//import com.kabu.bneibaruchseekers.core.model.data.journey.isIntro
//import com.kabu.bneibaruchseekers.core.ui.components.CustomCircularProgressIndicator
//import com.kabu.bneibaruchseekers.resources.Res
//import com.kabu.bneibaruchseekers.resources.intro_map
//import com.kabu.bneibaruchseekers.resources.map_order
//import com.kabu.bneibaruchseekers.resources.maps
//import com.kabu.bneibaruchseekers.resources.number_of_number
//import org.jetbrains.compose.resources.stringResource
//
//@Composable
//fun MapProgressView(
//    modifier: Modifier = Modifier,
//    journey: Journey,
//    enabled: Boolean = true,
//    onClick: () -> Unit = {},
//) {
//    ProgressView(
//        modifier = modifier.size(80.dp),
//        progress = journey.progress,
//        enabled = enabled,
//        onClick = onClick,
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center,
//        ) {
//            val text =
//                if (journey.isIntro) stringResource(Res.string.intro_map)
//                else stringResource(Res.string.map_order, journey.order)
//            Text(
//                text = text,
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.primary,
//                textAlign = TextAlign.Center,
//            )
////            Text(
////                text = stringResource(
////                    Res.string.number_of_number,
////                    journey.completedLessons,
////                    journey.totalLessons,
////                ),
////                style = MaterialTheme.typography.bodyLarge,
////                color = MaterialTheme.colorScheme.primary
////            )
//        }
//    }
//}
//
//@Composable
//fun MapsProgressView(
//    modifier: Modifier = Modifier,
//    journeyList: List<Journey>,
//    enabled: Boolean = true,
//    onClick: () -> Unit = {},
//) {
//    val completedJourneyList = journeyList.getCompletedJourneyList()
//    ProgressView(
//        modifier = modifier,
//        progress = (completedJourneyList.size / journeyList.size.toFloat()),
//        enabled = enabled,
//        onClick = onClick,
//    ) {
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Text(
//                text = stringResource(Res.string.maps),
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.secondary,
//            )
//            Text(
//                text = stringResource(
//                    Res.string.number_of_number,
//                    completedJourneyList.size,
//                    journeyList.size,
//                ),
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}
//
//@Composable
//fun ProgressView(
//    modifier: Modifier = Modifier,
//    progress: Float,
//    enabled: Boolean = true,
//    onClick: () -> Unit = {},
//    content: @Composable () -> Unit
//) {
//    Surface(
//        modifier = modifier,
//        enabled = enabled,
//        shape = CircleShape,
//        color = Color.Transparent,
//        onClick = onClick,
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center,
//        ) {
//            CustomCircularProgressIndicator(
//                modifier = Modifier
//                    .fillMaxSize(),
//                progress = progress,
//            )
//
//            content()
//        }
//    }
//}
