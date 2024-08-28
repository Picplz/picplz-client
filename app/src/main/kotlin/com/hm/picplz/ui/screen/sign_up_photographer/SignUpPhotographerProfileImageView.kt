package com.hm.picplz.ui.screen.sign_up_photographer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hm.picplz.ui.screen.sign_up.common.SignUpProfileImageView
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import com.hm.picplz.ui.screen.sign_up_photographer.SignUpPhotographerIntent.*

@Composable
fun SignUpPhotographerProfileImageView(
    modifier: Modifier = Modifier,
    currentState: SignUpPhotographerState,
    viewModel: SignUpPhotographerViewModel,
    innerPadding: PaddingValues,
) {
    /** 파일 피커 **/
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.handleIntent(SetProfileImageUri(uri))
        }
    }

    SignUpProfileImageView(
        modifier = modifier,
        innerPadding = innerPadding,
        currentNickname = currentState.nickname,
        currentImageUri = currentState.profileImageUri,
        onClickPrevIcon = { viewModel.handleIntent(ChangeStep(0)) },
        onClickBottomButton = { viewModel.handleIntent(ClickSubmitButton) },
        isBottomButtonEnabled = currentState.nickname.isNotEmpty() && currentState.profileImageUri != null,
        filePickerLauncher = filePickerLauncher,
    )
}