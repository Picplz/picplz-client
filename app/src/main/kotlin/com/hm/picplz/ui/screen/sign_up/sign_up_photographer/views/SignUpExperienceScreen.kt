package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignUpExperience(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    mainNavController: NavController,
    signUpPhotographerNavController: NavController,
) {
    val currentState = viewModel.state.collectAsState().value

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "경력 선택",
                onClickBack = {viewModel.handleIntent(NavigateToPrev)}
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }
            }
        }
    }
}