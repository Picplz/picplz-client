package com.hm.picplz.navigation.graph

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hm.picplz.BuildConfig
import com.hm.picplz.MyApplication
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.CancelReservation
import com.hm.picplz.navigation.model.CancelReservationConfirm
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailPhotographerSingleReview
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.DevMainSearchResultPreview
import com.hm.picplz.navigation.model.DevMyPagePackageEdit
import com.hm.picplz.navigation.model.DevMyPagePhotographerProfileAdded
import com.hm.picplz.navigation.model.Feed
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPageFollowedPhotographers
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageMyReviews
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.navigation.model.MyPagePackageEdit
import com.hm.picplz.navigation.model.MyPagePhotographer
import com.hm.picplz.navigation.model.MyPagePhotographerActiveAreaEdit
import com.hm.picplz.navigation.model.MyPagePhotographerAddDevice
import com.hm.picplz.navigation.model.MyPagePhotographerEquipmentSetting
import com.hm.picplz.navigation.model.MyPagePhotographerKeywordEdit
import com.hm.picplz.navigation.model.MyPagePhotographerModifyProfile
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.OrderDetail
import com.hm.picplz.navigation.model.PhotographerCancelReservation
import com.hm.picplz.navigation.model.PhotographerChatRoom
import com.hm.picplz.navigation.model.PhotographerDetailReservation
import com.hm.picplz.navigation.model.PhotographerRejectReservation
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.navigation.model.WriteReview
import com.hm.picplz.ui.screen.cancel_reservation.CancelReservationScreen
import com.hm.picplz.ui.screen.cancel_reservation_confirm.CancelReservationConfirmScreen
import com.hm.picplz.ui.screen.chat.ChatScreen
import com.hm.picplz.ui.screen.chat_room.ChatRoomScreen
import com.hm.picplz.ui.screen.detail_reservation.DetailReservationScreen
import com.hm.picplz.ui.screen.dev.DevScreen
import com.hm.picplz.ui.screen.feed.FeedScreen
import com.hm.picplz.ui.screen.main.MainScreen
import com.hm.picplz.ui.screen.main.MainSearchScreen
import com.hm.picplz.ui.screen.my_page.FollowedPhotographersScreen
import com.hm.picplz.ui.screen.my_page.MyPageModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageOrderSheetScreen
import com.hm.picplz.ui.screen.my_page.MyPagePackageDraft
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditIntent
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditMode
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditScreen
import com.hm.picplz.ui.screen.my_page.MyPagePackageEditState
import com.hm.picplz.ui.screen.my_page.MyPagePackageItem
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerActiveAreaEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerKeywordEditRoute
import com.hm.picplz.ui.screen.my_page.MyPagePhotographerModifyProfileScreen
import com.hm.picplz.ui.screen.my_page.MyPageScreen
import com.hm.picplz.ui.screen.my_page.MyPageShootingHistoryScreen
import com.hm.picplz.ui.screen.my_page.MyReviewScreen
import com.hm.picplz.ui.screen.order_detail.OrderDetailScreen
import com.hm.picplz.ui.screen.photographer_cancel_reservation.PhotographerCancelReservationScreen
import com.hm.picplz.ui.screen.photographer_chat_room.PhotographerChatRoomScreen
import com.hm.picplz.ui.screen.photographer_detail_reservation.PhotographerDetailReservationScreen
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentSettingScreen
import com.hm.picplz.ui.screen.photographer_main.composable.PhotographerAddDeviceScreen
import com.hm.picplz.ui.screen.photographer_reject_reservation.PhotographerRejectReservationScreen
import com.hm.picplz.ui.screen.reservation.ReservationScreen
import com.hm.picplz.ui.screen.write_review.WriteReviewScreen

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    composable<Dev> {
        val tokenManager = (LocalContext.current.applicationContext as MyApplication).tokenManager
        DevScreen(
            navController = navController,
            onLoginAsDevUser = tokenManager::setDevelopmentTokens,
            onLoginAsGuest = tokenManager::switchToGuestToken,
            onLogout = tokenManager::clearToken,
        )
    }

    composable<Main> { MainScreen(navController = navController) }

    composable<MainSearch> {
        MainSearchScreen(navController = navController)
    }

    if (BuildConfig.DEBUG) {
        composable<DevMainSearchResultPreview> {
            MainSearchScreen(
                navController = navController,
                devMockResults = true,
            )
        }
    }

    composable<Reservation> {
        ReservationScreen(navController = navController)
    }

    composable<Feed> {
        FeedScreen(navController = navController)
    }

    composable<Chat> {
        ChatScreen(navController = navController)
    }

    composable<ChatRoom> { backStackEntry ->
        val args = backStackEntry.toRoute<ChatRoom>()
        ChatRoomScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            _roomId = args.roomId,
        )
    }

    composable<PhotographerChatRoom> { backStackEntry ->
        val args = backStackEntry.toRoute<PhotographerChatRoom>()
        PhotographerChatRoomScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigatePhotographerDetailReservation = {
                navController.navigate(PhotographerDetailReservation)
            },
            _roomId = args.roomId,
        )
    }

    composable<MyPage> {
        MyPageScreen(navController = navController)
    }

    composable<MyPageFollowedPhotographers> {
        FollowedPhotographersScreen(navController = navController)
    }

    composable<MyPagePhotographer> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographer>()
        MyPageScreen(
            navController = navController,
            initialHasPhotographerRole = true,
            initialHasShootings = args.hasShootings,
            initialHasPackagePreview = args.hasPackagePreview,
            initialHasPortfolioPreview = args.hasPortfolioPreview,
        )
    }

    if (BuildConfig.DEBUG) {
        composable<DevMyPagePhotographerProfileAdded> { backStackEntry ->
            val args = backStackEntry.toRoute<DevMyPagePhotographerProfileAdded>()
            MyPageScreen(
                navController = navController,
                initialHasPhotographerRole = true,
                initialHasShootings = args.hasShootings,
                initialHasPackagePreview = args.hasPackagePreview,
                initialHasPortfolioPreview = args.hasPortfolioPreview,
            )
        }

        composable<DevMyPagePackageEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<DevMyPagePackageEdit>()
            val context = LocalContext.current
            var state by remember(args.packageCount) {
                mutableStateOf(devMyPagePackageEditState(args.packageCount))
            }
            MyPagePackageEditScreen(
                state = state,
                onIntent = { intent ->
                    when (intent) {
                        MyPagePackageEditIntent.ClickAddPackage -> {
                            state = state.openDevAddForm()
                        }
                        is MyPagePackageEditIntent.ChangePackageName -> {
                            state = state.updateDevDraft { it.copy(name = intent.value.take(15)) }
                        }
                        is MyPagePackageEditIntent.ChangeDescription -> {
                            state = state.updateDevDraft { it.copy(description = intent.value.take(300)) }
                        }
                        is MyPagePackageEditIntent.SelectDuration -> {
                            state =
                                state.updateDevDraft {
                                    it.copy(
                                        durationMinutes = intent.option.minutes,
                                        price = intent.option.price,
                                    )
                                }
                        }
                        is MyPagePackageEditIntent.ClickEditPackage,
                        is MyPagePackageEditIntent.RequestDeletePackage,
                        MyPagePackageEditIntent.ClickPackageImage,
                        MyPagePackageEditIntent.SavePackage,
                        -> {
                            Toast.makeText(
                                context,
                                context.getString(com.hm.picplz.feature.mypage.R.string.package_edit_option_pending),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        MyPagePackageEditIntent.NavigateBack -> {
                            if (state.editMode == MyPagePackageEditMode.List) {
                                navController.popBackStack()
                            } else {
                                state = devMyPagePackageEditState(args.packageCount)
                            }
                        }
                        else -> Unit
                    }
                },
            )
        }
    }

    composable<MyPageModifyProfile> {
        MyPageModifyProfileScreen(navController = navController)
    }

    composable<MyPagePhotographerModifyProfile> {
        MyPagePhotographerModifyProfileScreen(navController = navController)
    }

    composable<MyPagePhotographerKeywordEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerKeywordEdit>()
        MyPagePhotographerKeywordEditRoute(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<MyPagePhotographerEquipmentSetting> { backStackEntry ->
        val equipmentViewModel: PhotographerMainViewModel = hiltViewModel(backStackEntry)
        EquipmentSettingScreen(
            viewModel = equipmentViewModel,
            navController = navController,
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateAddDevice = { category ->
                navController.navigate(MyPagePhotographerAddDevice(category = category))
            },
        )
    }

    composable<MyPagePhotographerAddDevice> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerAddDevice>()
        val category =
            when (args.category.lowercase()) {
                "camera" -> DeviceCategory.CAMERA
                else -> DeviceCategory.PHONE
            }
        val equipmentBackStackEntry = navController.getBackStackEntry(MyPagePhotographerEquipmentSetting)
        val equipmentViewModel: PhotographerMainViewModel = hiltViewModel(equipmentBackStackEntry)
        PhotographerAddDeviceScreen(
            category = category,
            navController = navController,
            viewModel = equipmentViewModel,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPagePackageEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePackageEdit>()
        MyPagePackageEditRoute(
            photographerId = args.photographerId,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPagePhotographerActiveAreaEdit> { backStackEntry ->
        val args = backStackEntry.toRoute<MyPagePhotographerActiveAreaEdit>()
        MyPagePhotographerActiveAreaEditRoute(
            photographerId = args.photographerId,
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }

    composable<MyPageMyReviews> {
        MyReviewScreen(navController = navController)
    }

    composable<MyPageShootingHistory> { backStackEntry ->
        backStackEntry.toRoute<MyPageShootingHistory>()
        MyPageShootingHistoryScreen(navController = navController)
    }

    composable<MyPageOrderSheet> {
        MyPageOrderSheetScreen(navController = navController)
    }

    composable<DetailReservation> {
        DetailReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCancelReservation = { orderId ->
                navController.navigate(CancelReservation(orderId = orderId))
            },
            onNavigateToOrderDetail = { orderId ->
                navController.navigate(OrderDetail(orderId = orderId))
            },
            onNavigateToWriteReview = { orderId ->
                navController.navigate(WriteReview(orderId = orderId))
            },
        )
    }

    composable<WriteReview> {
        WriteReviewScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToReviewDetail = { reviewId ->
                // 리뷰 작성 화면은 백스택에서 정리하고 작가 리뷰 상세로 이동
                navController.navigate(
                    DetailPhotographerSingleReview(
                        reviewId = reviewId,
                        photoIndex = 0,
                        showRegisteredToast = true,
                    ),
                ) {
                    popUpTo<WriteReview> { inclusive = true }
                }
            },
        )
    }

    composable<PhotographerDetailReservation> {
        PhotographerDetailReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToRejectReason = { orderId ->
                navController.navigate(PhotographerRejectReservation(orderId = orderId))
            },
            onNavigateToCancelReservation = { orderId ->
                navController.navigate(PhotographerCancelReservation(orderId = orderId))
            },
        )
    }

    composable<PhotographerRejectReservation> {
        PhotographerRejectReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToChat = {
                // 예약이 시작된 채팅방으로 복귀 (거절 사유·예약 상세 화면 정리)
                navController.popBackStack<PhotographerChatRoom>(inclusive = false)
            },
        )
    }

    composable<PhotographerCancelReservation> {
        PhotographerCancelReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCancelConfirm = {
                navController.navigate(CancelReservationConfirm(isPhotographer = true))
            },
        )
    }

    composable<CancelReservationConfirm> { backStackEntry ->
        val args = backStackEntry.toRoute<CancelReservationConfirm>()
        CancelReservationConfirmScreen(
            isPhotographer = args.isPhotographer,
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateHome = {
                navController.navigate(Main) {
                    popUpTo<Dev> { inclusive = false }
                }
            },
        )
    }

    composable<OrderDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<OrderDetail>()
        OrderDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateNextStep = {
                navController.navigate(CancelReservation(orderId = args.orderId))
            },
        )
    }

    composable<CancelReservation> {
        CancelReservationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCancelConfirm = {
                navController.navigate(CancelReservationConfirm(isPhotographer = false))
            },
        )
    }
}

private fun devMyPagePackageEditState(packageCount: Int): MyPagePackageEditState =
    MyPagePackageEditState(
        photographerId = 1L,
        packages = devPackageEditItems.take(packageCount.coerceIn(0, devPackageEditItems.size)),
    )

private fun MyPagePackageEditState.openDevAddForm(): MyPagePackageEditState =
    copy(
        editMode = MyPagePackageEditMode.Add,
        draft = MyPagePackageDraft(),
        originalDraft = MyPagePackageDraft(),
        editingPackageId = null,
        isSaveEnabled = false,
    )

private fun MyPagePackageEditState.updateDevDraft(
    transform: (MyPagePackageDraft) -> MyPagePackageDraft,
): MyPagePackageEditState {
    val updatedDraft = transform(draft)
    return copy(
        draft = updatedDraft,
        isSaveEnabled = updatedDraft.hasRequiredFields,
    )
}

private val devPackageEditItems =
    listOf(
        MyPagePackageItem(
            id = 1L,
            name = "남친 생기는 프사",
            description = "카톡 프로필용 자연광 촬영 패키지입니다.",
            durationMinutes = 15,
            price = 12900,
            imageUri = "https://picsum.photos/seed/package-edit-1/900/390",
            imageObjectKey = null,
        ),
        MyPagePackageItem(
            id = 2L,
            name = "서울숲 자연광 스냅",
            description = "서울숲 산책 코스에서 자연스럽게 촬영해요.",
            durationMinutes = 30,
            price = 18900,
            imageUri = "https://picsum.photos/seed/package-edit-2/900/390",
            imageObjectKey = null,
        ),
        MyPagePackageItem(
            id = 3L,
            name = "성수 감성 프로필",
            description = "성수 골목과 카페 무드로 프로필을 촬영해요.",
            durationMinutes = 60,
            price = 22900,
            imageUri = "https://picsum.photos/seed/package-edit-3/900/390",
            imageObjectKey = null,
        ),
    )
