package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpenseTracker() {
    var selectedButton by remember { mutableStateOf("This Week") }
    var selectedDay by remember { mutableStateOf("Mon") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header
        Text(
            text = "Expense Tracker",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Time Period Toggle Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedButton = "This Year" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "This Year") Color.Cyan else Color.LightGray
                )
            ) {
                Text("This Year")
            }
            Button(
                onClick = { selectedButton = "This Month" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "This Month") Color.Cyan else Color.LightGray
                )
            ) {
                Text("This Month")
            }
            Button(
                onClick = { selectedButton = "This Week" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "This Week") Color.Cyan else Color.LightGray
                )
            ) {
                Text("This Week")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Circular Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(width = 4.dp, color = Color.Red, shape = CircleShape)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "NTD 30000", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(width = 4.dp, color = Color.Green, shape = CircleShape)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "NTD 50000", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Week Days
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeekDayItem(day = "Mon", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Tue", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Wed", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Thu", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Fri", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Sat", selectedDay = selectedDay) { selectedDay = it }
            WeekDayItem(day = "Sun", selectedDay = selectedDay) { selectedDay = it }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Expense List
        Column(modifier = Modifier.fillMaxWidth()) {
            ExpenseItem("Salary", 55000, Color.Green)
            ExpenseItem("Tuition Fee", 30000, Color.Red)
            ExpenseItem("Clothes", 2000, Color.Red)
            ExpenseItem("Food", 100, Color.Red)
            ExpenseItem("Food", 100, Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Button
        Button(onClick = { /* Handle Add */ }, modifier = Modifier.align(Alignment.End)) {
            Text("Add")
        }
    }
}

@Composable
fun WeekDayItem(day: String, selectedDay: String, onDaySelected: (String) -> Unit) {
    Text(
        text = day,
        color = if (day == selectedDay) Color.Cyan else Color.Black,
        fontWeight = if (day == selectedDay) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.clickable { onDaySelected(day) }
    )
}

@Composable
fun ExpenseItem(name: String, amount: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, fontSize = 16.sp)
        Text(
            text = amount.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

