package com.ndhu.moneymanagementapp.graphs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun PieChart(
    entries: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = entries.sum()
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Canvas(modifier = modifier) {
        val diameter = size.minDimension
        val center = Offset(size.width / 2, size.height / 2)

        var startAngle = -90f
        entries.forEachIndexed { index, value ->
            val sweepAngle = 360f * (value / total) * animatedProgress.value
            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - diameter / 2, center.y - diameter / 2),
                size = Size(diameter, diameter)
            )
            startAngle += sweepAngle
        }
    }
}
