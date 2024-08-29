package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameView
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel

@Composable
fun SignUpPhotographerNicknameView (
    modifier: Modifier = Modifier,
    currentState: SignUpPhotographerState,
    viewModel: SignUpPhotographerViewModel,
    innerPadding: PaddingValues
) {
    SignUpNicknameView(
        modifier = modifier,
        currentNickname = currentState.nickname,
        onClickBackIcon = { viewModel.handleIntent(SignUpPhotographerIntent.NavigateToPrev) },
        onNicknameFieldChange = { newValue -> viewModel.handleIntent(
            SignUpPhotographerIntent.SetNickname(
                newValue
            )
        ) },
        onClickBottomButton = { viewModel.handleIntent(SignUpPhotographerIntent.ChangeStep(1))},
        innerPadding = innerPadding,
    )
}