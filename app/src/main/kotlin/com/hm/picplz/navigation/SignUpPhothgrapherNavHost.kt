package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpDetailExpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpExperienceScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views.SignUpPhotographyVibeScreen
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel

@Composable
fun SignUpPhotographerNavHost(
    mainNavController: NavHostController,
    signUpPhotographerNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel()
) {
    NavHost(
        navController = signUpPhotographerNavController,
        startDestination = "sign-up-experience",
        modifier = modifier,
    ) {
        composable("sign-up-experience") {
            SignUpExperienceScreen(
                modifier = modifier,
                mainNavController = mainNavController,
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel,
            )
        }
        composable("sign-up-detail-experience") {
            SignUpDetailExpScreen(
                modifier = modifier,
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel
            )
        }
        composable("sign-up-photography-vibe") {
            SignUpPhotographyVibeScreen(
                modifier = modifier,
                signUpPhotographerNavController = signUpPhotographerNavController,
                viewModel = viewModel
            )
        }
    }
}