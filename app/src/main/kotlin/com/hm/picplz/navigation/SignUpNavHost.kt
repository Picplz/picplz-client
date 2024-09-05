package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpNicknameScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpProfileImageScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpSelectTypeScreen
import com.hm.picplz.viewmodel.SignUpCommonViewModel

@Composable
fun SignUpNavHost(
    mainNavController: NavHostController,
    signUpNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
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