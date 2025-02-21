package com.ndhu.moneymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.google.firebase.FirebaseApp
import com.ndhu.moneymanagementapp.ui.pages.FirstLogin
import com.ndhu.moneymanagementapp.ui.pages.Homepage
import com.ndhu.moneymanagementapp.ui.pages.SignIn
import com.ndhu.moneymanagementapp.ui.pages.SignUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MaterialTheme {
                // Homepage()
                SignUp()
            }
        }
    }
}