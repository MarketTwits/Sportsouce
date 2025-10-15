//package com.kabu.bneibaruchseekers.core.ui.components.journey
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.kabu.bneibaruchseekers.resources.Res
//import com.kabu.bneibaruchseekers.resources.intro
//import com.kabu.bneibaruchseekers.resources.map_order
//import dev.chrisbanes.haze.HazeDefaults
//import dev.chrisbanes.haze.HazeStyle
//import dev.chrisbanes.haze.HazeTint
//import dev.chrisbanes.haze.hazeEffect
//import dev.chrisbanes.haze.hazeSource
//import dev.chrisbanes.haze.rememberHazeState
//import org.jetbrains.compose.resources.stringResource
//
//@Composable
//fun JourneyOrderView(
//    modifier: Modifier = Modifier,
//    order: Int,
//) {
//    val hazeState = rememberHazeState()
//    Box(
//        modifier = modifier,
//    ) {
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .hazeSource(state = hazeState)
//        )
//        val style = HazeStyle(
//            backgroundColor = Color.White.copy(0.05f),
//            tints = listOf(HazeTint(Color.White.copy(alpha = 0.05f))),
//            blurRadius = 12.dp,
//            noiseFactor = HazeDefaults.noiseFactor,
//        )
//        Box(
//            modifier = Modifier
//                .height(28.dp)
//                .clip(RoundedCornerShape(100))
//                .hazeEffect(hazeState, style = style),
//            contentAlignment = Alignment.Center,
//        ) {
//            Box(Modifier.padding(horizontal = 12.dp)) {
//                if (order == 0) {
//                    Text(
//                        text = stringResource(Res.string.intro),
//                        style = MaterialTheme.typography.titleSmall,
//                    )
//                } else {
//                    Text(
//                        text = stringResource(Res.string.map_order, order),
//                        style = MaterialTheme.typography.titleSmall,
//                    )
//                }
//            }
//        }
//    }
//}