package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.enums.AppScreens

@Composable
fun PendingRequestScreen (context: Context, navController: NavController)
{
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate(route = AppScreens.ContactScreen.name) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Contact Screen",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Pending Request", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            } // Row
        }, // Top Bar

        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
                    .height(100.dp)
            ) {
                // Reject All
                Button(
                    onClick = { /* TODO: Reject All Request  */ },
                    modifier = Modifier
                        .width(150.dp)
                        .height(75.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF961400), // Red
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Reject All", fontSize = 16.sp)
                }

                // Accept All
                Button(
                    onClick = { /* TODO: Accept All Request  */ },
                    modifier = Modifier
                        .width(150.dp)
                        .height(75.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF009669), // Green
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Accept All", fontSize = 16.sp)
                }
            }
        } // Bottom Bar

    ) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues)
            .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBox()

            Spacer(modifier = Modifier.width(8.dp))

            val contacts = listOf("New Contact 1", "New Contact 2", "New Contact 3")

            // Use Card to make rounded corner for LazyColumn
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBDBDBD)),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(contacts) { contact ->
                        ContactItem(contact)
                    }
                }
            } // Card

        }

    } // Scaffold
}