package com.hm.picplz.ui.screen.sign_up.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.R
import com.hm.picplz.data.model.UserType
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonSelectImageButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.ClickUserTypeButton
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.NavigateToSelected
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SignUpViewModel

@Composable
fun SignUpSelectTypeView(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    innerPadding: PaddingValues,
    selectedUserType: UserType?,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTopBar(
            text = "회원 타입 선택",
            onClickBack = { viewModel.handleIntent(NavigateToPrev) },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("회원 타입을\n")
                        append("선택해주세요")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )
                Column(
                    modifier = Modifier,
                ) {
                    CommonSelectImageButton(
                        text = "고객",
                        isSelected = selectedUserType == UserType.User,
                        onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.User)) },
                        iconResId = R.drawable.logo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CommonSelectImageButton(
                        text = "금손",
                        isSelected = selectedUserType == UserType.Photographer,
                        onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.Photographer)) },
                        iconResId = R.drawable.logo
                    )
                }
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
                text = "다음",
                onClick = { viewModel.handleIntent(NavigateToSelected) },
                enabled = selectedUserType != null,
                containerColor = MainThemeColor.Black
            )
        }
    }
}