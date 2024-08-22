package com.hm.picplz.ui.screen.sign_up_client

import SignUpClientNicknameView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.viewmodel.emptyUserData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpClientScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userInfo: User = emptyUserData,
    viewModel: SignUpClientViewModel = viewModel(),
) {
    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        when (currentState.currentStep) {
            0 -> SignUpClientNicknameView(
                modifier = modifier,
                currentState = currentState,
                viewModel = viewModel,
                innerPadding = innerPadding
            )
            1 -> SignUpClientProfileImageView(
                modifier = modifier,
                currentState = currentState,
                viewModel = viewModel,
                innerPadding = innerPadding
            )
            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpClientSideEffect.SubmitProfileInfo -> {}
                is SignUpClientSideEffect.ShowFileUploadDialog -> {}
                is SignUpClientSideEffect.NavigateToPrev-> {
                    navController.popBackStack()
                }
            }
        }
    }
}