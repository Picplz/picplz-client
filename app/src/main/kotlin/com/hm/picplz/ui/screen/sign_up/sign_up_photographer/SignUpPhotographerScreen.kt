package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import com.hm.picplz.viewmodel.emptyUserData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpPhotographerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userInfo: User = emptyUserData,
    viewModel: SignUpPhotographerViewModel = viewModel(),
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
                text = "금손 선택",
                onClickBack = {}
            )
        }
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                is SignUpPhotographerSideEffect.ShowFileUploadDialog -> {

                }
                is SignUpPhotographerSideEffect.SubmitProfileInfo -> {
                    Toast.makeText(context, "가입", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}