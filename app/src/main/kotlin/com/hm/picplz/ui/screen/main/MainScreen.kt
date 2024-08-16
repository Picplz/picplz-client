package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Scaffold (modifier = Modifier.fillMaxSize()) {innerPadding ->
        Text(
            text = "메인 화면",
            modifier = Modifier.padding(innerPadding)
        )
    }
}