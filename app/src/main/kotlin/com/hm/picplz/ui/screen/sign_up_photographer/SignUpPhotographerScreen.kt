package com.hm.picplz.ui.screen.sign_up_photographer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpPhotographerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userInfo: User,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    ) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CommonTopBar(
                text = "활동 지역 선택",
                onClickBack = { viewModel.handleIntent(SignUpPhotographerIntent.NavigateToPrev) },
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev-> {
                    navController.popBackStack()
                }
            }
        }
    }
}