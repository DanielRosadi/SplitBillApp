package com.ndhu.moneymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.ndhu.moneymanagementapp.handlers.FirebaseHandler
import com.ndhu.moneymanagementapp.navigation.AppNavigation
import com.ndhu.moneymanagementapp.ui.pages.LogIn
import com.ndhu.moneymanagementapp.utils.Util
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                val context = this
                var userId by remember { mutableStateOf<String?>(null) }
                var isConnected by remember { mutableStateOf<Boolean?>(null) }
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        isConnected = FirebaseHandler.isConnected()
                        userId = Util.getUserId(context)
                    }
                }
                // Homepage()
                // SignUp()

                // ListStoresPage()
                AppNavigation(context)
//                LogIn(context, NavController(context))
            }
        }
    }
}
