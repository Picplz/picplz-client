package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonChip
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpPhotographyVibeScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    signUpPhotographerNavController: NavController
){
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit){
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
    }

    val currentState = viewModel.state.collectAsState().value

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    viewModel.handleIntent(SetEditingChipId(null))
                })
            },
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
                text = "경력 선택",
                onClickBack = {viewModel.handleIntent(NavigateToPrev)}
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 15.dp)
                    .imePadding()
                    .verticalScroll(scrollState),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(80.dp)
                    )
                    Text(
                        text = "자신 있는 촬영 감성을 선택해 주세요.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp)
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        currentState.vibeChipList.map { chip ->
                            CommonChip(
                                id = chip.id,
                                label = chip.label,
                                initialMode = ChipMode.DEFAULT,
                                isEditing = currentState.editingChipId == chip.id,
                                isEditable = chip.isEditable,
                                onClickDefaultMode = {
                                    focusManager.clearFocus()
                                    viewModel.handleIntent(SetEditingChipId(null))
                                    viewModel.handleIntent(UpdateSelectedVibeChipList(chipId = chip.id, label = chip.label))
                                },
                                isSelected = currentState.selectedVibeChipList.any { it.id == chip.id },
                                onUpdate = { value ->
                                    viewModel.handleIntent(UpdateVibeChip(chip.id, value))
                                },
                                onEdit = {
                                    viewModel.handleIntent(SetEditingChipId(chip.id))
                                },
                                onEndEdit = {
                                    focusManager.clearFocus()
                                    viewModel.handleIntent(SetEditingChipId(null))
                                }
                            )
                        }
                        CommonChip(
                            id = "ADD_1",
                            initialMode = ChipMode.ADD,
                            isEditing = currentState.editingChipId == "ADD_1",
                            onEdit = {
                                viewModel.handleIntent(SetEditingChipId("ADD_1"))
                            },
                            onAdd = {value ->
                                viewModel.handleIntent(AddVibeChip(value))
                                viewModel.handleIntent(SetEditingChipId(null))
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonBottomButton(
                    text = "다음",
                    onClick = {},
                    enabled = currentState.selectedVibeChipList != listOf<ChipItem>(),
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                is SignUpPhotographerSideEffect.Navigate -> {
                    signUpPhotographerNavController.navigate(sideEffect.destination)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SignUpPhotographyVibeScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()
        SignUpPhotographyVibeScreen(
            signUpPhotographerNavController = signUpPhotographerNavController
        )
    }
}