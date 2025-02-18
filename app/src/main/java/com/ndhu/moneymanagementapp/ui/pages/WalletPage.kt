package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WalletPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Wallet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wallet Options Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WalletOption(title = "Cash", value = 9999)
            WalletOption(title = "Chunghwa Post", value = 99999)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Handle add action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan,
                    contentColor = Color.Black
                ),
                shape = CircleShape,
                modifier = Modifier
                    .width(100.dp)  // Increased width for oval shape
                    .height(40.dp)  // Keep the height smaller
            ) {
                Text(text = "Add", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Report Section
        Text(
            text = "Report this month",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for Graph
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            DrawPlaceholderGraph()
        }
    }
}

@Composable
fun WalletOption(title: String, value: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween, // Ensures left and right alignment
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(1f) // Takes up remaining space to push text to the right
        ) {
            Canvas(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            ) {
                drawCircle(
                    color = Color.Black,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Value text aligned to the right
        Text(
            text = value.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DrawPlaceholderGraph() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw axes
        drawLine(
            color = Color.DarkGray,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(0f, 0f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.DarkGray,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Draw graph line
        drawLine(
            color = Color.Cyan,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Cyan,
            start = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height / 3),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
}
