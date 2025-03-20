package com.ndhu.moneymanagementapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

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

