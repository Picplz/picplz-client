package com.hm.picplz.ui.screen.sign_up.common

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun SignUpProfileImageView(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    currentNickname: String,
    currentImageUri: Uri?,
    onClickBottomButton: () -> Unit,
    onClickPrevIcon: () -> Unit,
    isBottomButtonEnabled: Boolean = currentImageUri != null,
    filePickerLauncher: ManagedActivityResultLauncher<String, Uri?>,
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
            onClickBack = { onClickPrevIcon() }
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
                    val painter = if (currentImageUri != null) {
                        rememberAsyncImagePainter(model = currentImageUri)
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
                    text = "${currentNickname}님 안녕하세요",
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
                onClick = { onClickBottomButton() },
                enabled = isBottomButtonEnabled,
                containerColor = MainThemeColor.Black
            )
        }
    }
}