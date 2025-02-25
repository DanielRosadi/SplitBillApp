package com.ndhu.moneymanagementapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp
import com.ndhu.moneymanagementapp.handlers.FirebaseHandler
import com.ndhu.moneymanagementapp.seeds.Seeders
import com.ndhu.moneymanagementapp.ui.pages.FirstLogin
import com.ndhu.moneymanagementapp.ui.pages.Homepage
import com.ndhu.moneymanagementapp.ui.pages.SignIn
import com.ndhu.moneymanagementapp.ui.pages.SignUp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                var isConnected by remember { mutableStateOf<Boolean?>(null) }
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        isConnected = FirebaseHandler.isConnected()
//                        Seeders.runSeeder() //uncomment this if u want to run seeder
                    }
                }
                // Homepage()
                SignUp()
            }
        }
    }
}