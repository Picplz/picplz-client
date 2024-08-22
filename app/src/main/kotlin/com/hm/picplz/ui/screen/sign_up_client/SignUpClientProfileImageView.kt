package com.hm.picplz.ui.screen.sign_up_client

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpClientViewModel

@Composable
fun SignUpClientProfileImageView (
    modifier: Modifier = Modifier,
    currentState: SignUpClientState,
    viewModel: SignUpClientViewModel,
    innerPadding: PaddingValues
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTopBar(
            text = "프로필 이미지 업로드",
            onClickBack = { viewModel.handleIntent(SignUpClientIntent.ChangeStep(0)) }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {}
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpClientProfileImageViewPreview () {
    val viewModel: SignUpClientViewModel = viewModel()
    val currentState = viewModel.state.collectAsState().value
    PicplzTheme {
        SignUpClientProfileImageView(
            currentState = currentState,
            viewModel = viewModel,
            innerPadding = PaddingValues() ,
        )
    }
}