package com.hm.picplz.ui.screen.sign_up_client

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.screen.sign_up.common.SignUpProfileImageView
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent.*

@Composable
fun SignUpClientProfileImageView(
    modifier: Modifier = Modifier,
    currentState: SignUpClientState,
    viewModel: SignUpClientViewModel,
    innerPadding: PaddingValues
) {
    /** 파일 피커 **/
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.handleIntent(SetProfileImageUri(uri))        }
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

@Preview(showBackground = true)
@Composable
fun SignUpClientProfileImageViewPreview() {
    val viewModel: SignUpClientViewModel = viewModel()
    val currentState = viewModel.state.collectAsState().value
    PicplzTheme {
        SignUpClientProfileImageView(
            currentState = currentState,
            viewModel = viewModel,
            innerPadding = PaddingValues()
        )
    }
}