package com.ndhu.moneymanagementapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UniformDesignScaffold(topAppBar: @Composable () -> Unit = {}, bottomBar: @Composable () -> Unit = {}, body: @Composable (PaddingValues) -> Unit = {}){
    Scaffold (
        topBar = {topAppBar()},
        bottomBar = {bottomBar()}
    )
    {paddingValues ->
        body(paddingValues)
    }
}


val topBar = @Composable { navController: NavController ->
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(50.dp))
        Text("List of Store", fontSize = 40.sp, fontWeight = FontWeight.Bold)
    }
}
