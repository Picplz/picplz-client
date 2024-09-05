package com.hm.picplz.ui.screen.sign_up.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.MainActivity
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpProfileImageScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    navController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
    }

    /** 파일 피커 **/
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.handleIntent(SetProfileImageUri(uri))
        }
    }

    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "프로필 이미지 업로드",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = if (currentState.profileImageUri != null) {
                            rememberAsyncImagePainter(model = currentState.profileImageUri)
                        } else {
                            painterResource(id = R.drawable.logo_temp)
                        }
                        Image(
                            painter = painter,
                            contentDescription = "프로필 이미지",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )

                        IconButton(
                            onClick = { viewModel.handleIntent(ShowFileUploadDialog) },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(40.dp)
                                .offset(x = 0.dp, y = 0.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "이미지 업로드",
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Gray, CircleShape)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(50.dp)
                    )
                    Text(
                        text = "${currentState.nickname}님 안녕하세요",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonButton(
                    text = "완료하기",
                    onClick = { viewModel.handleIntent(Navigate("sign-up-select-type")) },
                    enabled = currentState.nickname.isNotEmpty() && currentState.profileImageUri != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                is SignUpSideEffect.Navigate -> {
                    navController.navigate(sideEffect.destination)
                }
                is SignUpSideEffect.ShowFileUploadDialog -> {
                    filePickerLauncher.launch("image/*")
                }
                else -> {}
            }
        }
    }
}