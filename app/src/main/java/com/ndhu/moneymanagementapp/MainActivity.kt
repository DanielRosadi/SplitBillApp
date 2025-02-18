package com.ndhu.moneymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.ndhu.moneymanagementapp.ui.pages.FirstLogin
import com.ndhu.moneymanagementapp.ui.pages.Homepage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Homepage()
            }
        }
    }
}