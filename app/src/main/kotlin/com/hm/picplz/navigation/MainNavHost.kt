package com.hm.picplz.navigation

import LoginScreen
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerScreen
import com.hm.picplz.viewmodel.MainActivityUiState
import com.hm.picplz.viewmodel.emptyUserData

@Composable
fun MainNavHost(
    navController: NavHostController,
    uiState: MainActivityUiState,
    modifier: Modifier = Modifier
) {
    val startDestination = when (uiState) {
        is MainActivityUiState.Success -> "main"
        else -> "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable("login") { LoginScreen(navController = navController) }
        composable("main") { MainScreen(navController = navController) }
        composable("sign-up") { SignUpScreen(mainNavController = navController) }
        composable("sign-up-client") { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpClientScreen(
                navController = navController,
                userInfo = userInfo ?: emptyUserData
            )
        }

        composable("sign-up-photographer") { backStackEntry ->
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("userInfo", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                backStackEntry.arguments?.getParcelable("userInfo")
            }
            SignUpPhotographerScreen(
                mainNavController = navController,
                userInfo = userInfo ?: emptyUserData
            )
        }
    }
}