package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountScreen() {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    selected = false,
                    onClick = { /* Navigate to Home */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "List") },
                    selected = false,
                    onClick = { /* Navigate to List */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") },
                    selected = false,
                    onClick = { /* Navigate to Cart */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    selected = false,
                    onClick = { /* Navigate to Settings */ }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF00C896), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(50.dp),
                    tint = Color.Gray
                )
            }

            // Name
            Text(
                text = "Full name",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            // Bio
            Text(
                text = "Bio",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Settings
            Text(
                text = "Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Log Out
            OutlinedButton(
                onClick = { /* Log out logic */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Log Out")
            }

            // Additional Options
            Text(
                text = "Additional Options",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            repeat(4) { index ->
                OutlinedButton(
                    onClick = { /* Handle Option ${index + 1} */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Option ${index + 1}")
                }
            }

            // Footer
            Text(
                text = "Footer Information",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
