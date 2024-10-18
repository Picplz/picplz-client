package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.SignUpCommonNavHost
import com.hm.picplz.viewmodel.SignUpCommonViewModel


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavHostController,
) {
    val signUpCommonNavController = rememberNavController()

    SignUpCommonNavHost(
        mainNavController = mainNavController,
        signUpCommonNavController = signUpCommonNavController,
        viewModel = viewModel,
        modifier = modifier,
    )
}