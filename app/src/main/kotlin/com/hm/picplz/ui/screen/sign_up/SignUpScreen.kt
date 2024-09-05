package com.hm.picplz.ui.screen.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hm.picplz.viewmodel.SignUpViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.SignUpNavHost
import com.hm.picplz.ui.theme.PicplzTheme


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    mainNavController: NavHostController,
) {
    val signUpNavController = rememberNavController()

    SignUpNavHost(
        mainNavController = mainNavController,
        signUpNavController = signUpNavController,
        viewModel = viewModel
    )
}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        SignUpScreen(mainNavController = navController)
    }
}