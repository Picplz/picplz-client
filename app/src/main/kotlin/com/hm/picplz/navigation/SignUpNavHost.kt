package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameScreen
import com.hm.picplz.ui.screen.sign_up.common.SignUpProfileImageScreen
import com.hm.picplz.ui.screen.sign_up.common.SignUpSelectTypeScreen
import com.hm.picplz.viewmodel.SignUpViewModel

@Composable
fun SignUpNavHost(
    mainNavController: NavHostController,
    signUpNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
) {
    NavHost(
        navController = signUpNavController,
        startDestination = "sign-up-nickname",
        modifier = modifier,
    ) {
        composable("sign-up-nickname") {
            SignUpNicknameScreen(
                mainNavController = mainNavController,
                signUpNavController = signUpNavController,
                viewModel = viewModel
            )
        }
        composable("sign-up-profile") {
            SignUpProfileImageScreen(
                navController = signUpNavController,
                viewModel = viewModel
            )
        }
        composable("sign-up-select-type") {
            SignUpSelectTypeScreen(
                mainNavController = mainNavController,
                signUpNavController = signUpNavController,
                viewModel = viewModel
            )
        }
    }
}