package com.hm.picplz.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.sign_up.SignUpScreen
import com.hm.picplz.viewmodel.MainActivityUiState

@Composable
fun AppNavHost(
    navController: NavHostController,
    uiState: MainActivityUiState,
    modifier: Modifier = Modifier
) {
    val startDestination = when (uiState) {
        is MainActivityUiState.Unauthenticated -> "login"
        else -> "main"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") { LoginScreen(navController = navController) }
        composable("main") { MainScreen(navController = navController) }
        composable("sign-up") { SignUpScreen(navController = navController)}
    }
}