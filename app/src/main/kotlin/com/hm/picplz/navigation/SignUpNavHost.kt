package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.ChangeStep
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.SetNickname
import com.hm.picplz.ui.screen.sign_up.SignUpState
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameScreen

@Composable
fun SignUpNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "sign-up-nickname",
        modifier = modifier,
    ) {
        composable("sign-up-nickname") {
            SignUpNicknameScreen(
                navController = navController
            )
        }
//        composable("sign-up-profile") {
//            SignUpProfileScreen(
//                onNavigateNext = { signUpNavController.navigate("sign-up-select-type") },
//                onNavigateBack = { signUpNavController.popBackStack() }
//            )
//        }
//        composable("sign-up-select-type") {
//            SignUpSelectTypeScreen(
//                onNavigateBack = { signUpNavController.popBackStack() },
//                onNavigateComplete = { userType ->
//                    // 회원가입 완료 후 상위 네비게이션으로 이동
//                    parentNavController.navigate("profile") {
//                        popUpTo("home")
//                    }
//                }
//            )
//        }
    }
}