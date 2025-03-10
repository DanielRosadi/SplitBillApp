package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplitBill(context : Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Split Bill",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Circle indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CircleIndicator(amount = "NTD 30000", borderColor = Color.Red)
            CircleIndicator(amount = "NTD 50000", borderColor = Color.Green)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // "People You Owe" Section
        Text(
            text = "People You Owe",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        // Debt list
        DebtRow(name = "Nat", amount = "55000", textColor = Color.Red)

        Spacer(modifier = Modifier.height(24.dp))

        // "People Owe You" Section
        Text(
            text = "People Owe You",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Green,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        // Credit list
        DebtRow(name = "Daniel", amount = "55000", textColor = Color.Green)
        Spacer(modifier = Modifier.height(8.dp))
        DebtRow(name = "Steven", amount = "1000", textColor = Color.Green)

        Spacer(modifier = Modifier.height(24.dp))

        // Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Add action */ },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Add", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun CircleIndicator(amount: String, borderColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
            .border(
                width = 4.dp,
                color = borderColor,
                shape = CircleShape
            )
    ) {
        Text(
            text = amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DebtRow(name: String, amount: String, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
