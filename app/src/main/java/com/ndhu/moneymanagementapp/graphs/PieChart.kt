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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin

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

            drawIntoCanvas { canvas ->
                val labelAngle = startAngle + sweepAngle / 2
                val labelRadius = diameter / 2.5f // place label slightly inside

                val labelX = center.x + labelRadius * cos(Math.toRadians(labelAngle.toDouble())).toFloat()
                val labelY = center.y + labelRadius * sin(Math.toRadians(labelAngle.toDouble())).toFloat()

                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 40f
                    isAntiAlias = true
                }

                canvas.nativeCanvas.drawText(
                    "NTD ${value.toInt()}",
                    labelX,
                    labelY + paint.textSize / 3f, // baseline adjustment
                    paint
                )
            }


            startAngle += sweepAngle
        }
    }
}
