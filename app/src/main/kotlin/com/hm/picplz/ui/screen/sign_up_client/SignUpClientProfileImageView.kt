package com.hm.picplz.ui.screen.sign_up_client

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
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
            viewModel.handleIntent(SetProfileImageUri(uri))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTopBar(
            text = "프로필 이미지 업로드",
            onClickBack = { viewModel.handleIntent(ChangeStep(0)) }
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
                        onClick = {
                            filePickerLauncher.launch("image/*")
                        },
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
        ) {}
    }
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