package com.ndhu.moneymanagementapp.ui.pages.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen

@Composable
fun WelcomeScreen(navController: NavController) {
    AuthScreen(
        screenTitle = "Welcome",
        showCard = false,
        formContent = {
            Spacer(modifier = Modifier.weight(1f)) // Push content down from top

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Split expenses with ease.",
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Log In Button (green)
                Button(
                    onClick = { navController.navigate(AppScreens.LogInScreen.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                ) {
                    Text("Log In", color = Color.White)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                    Text(
                        text = "  or  ",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                }

                // Create Account Button (gray)
                Button(
                    onClick = { navController.navigate(AppScreens.SignUpScreen.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
                ) {
                    Text("Create Account", color = Color.White)
                }

            }

            Spacer(modifier = Modifier.weight(1f)) // Push content up from bottom
        },
        footerContent = {}
    )
}