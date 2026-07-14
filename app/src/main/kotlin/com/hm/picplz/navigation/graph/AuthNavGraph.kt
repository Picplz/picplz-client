package com.hm.picplz.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.hm.picplz.BuildConfig
import com.hm.picplz.navigation.UserTypeMap
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.SignUpCompletion
import com.hm.picplz.navigation.model.SignUpIntro
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.screen.login.LoginIntroScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.SignUpCompletionScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onLoginCompleted: () -> Unit,
    onSignupCompleted: () -> Unit,
) {
    composable<Login> {
        LoginIntroScreen(
            navController = navController,
            onLoginCompleted = onLoginCompleted,
            enableDevEntry = BuildConfig.DEBUG,
        )
    }

    composable<SignUpIntro>(
        deepLinks =
            listOf(
                navDeepLink {
                    uriPattern = "picplz://signup/common/{startAt}"
                },
            ),
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpIntro>()
        SignUpScreen(
            mainNavController = navController,
            profileImageUri = args.profileImageUri,
            startAt = args.startAt,
        )
    }

    composable<SignUpPhotographer>(
        typeMap = UserTypeMap,
        deepLinks =
            listOf(
                navDeepLink {
                    uriPattern = "picplz://signup/photographer/{startAt}"
                },
            ),
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpPhotographer>()
        SignUpPhotographerScreen(
            mainNavController = navController,
            userInfo = args.userInfo,
            startAt = args.startAt,
        )
    }

    composable<SignUpCompletion>(typeMap = UserTypeMap) { backStackEntry ->
        val args = backStackEntry.toRoute<SignUpCompletion>()
        SignUpCompletionScreen(
            mainNavController = navController,
            userInfo = args.userInfo,
            onSignupCompleted = onSignupCompleted,
        )
    }
}
