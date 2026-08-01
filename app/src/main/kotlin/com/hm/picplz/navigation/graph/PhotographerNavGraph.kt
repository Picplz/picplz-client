package com.hm.picplz.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.DetailPhotographerPhotoPortfolios
import com.hm.picplz.navigation.model.DetailPhotographerPhotoReviews
import com.hm.picplz.navigation.model.DetailPhotographerPortfolioDetail
import com.hm.picplz.navigation.model.DetailPhotographerSingleReview
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPagePhotographer
import com.hm.picplz.navigation.model.PhotographerAddDevice
import com.hm.picplz.navigation.model.PhotographerEquipmentSetting
import com.hm.picplz.navigation.model.PhotographerMain
import com.hm.picplz.navigation.model.PhotographerMainGraph
import com.hm.picplz.navigation.model.QuickShoot
import com.hm.picplz.navigation.model.ReviewPhotographer
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPhotoReviewsScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerPortfoliosScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerScreen
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerSingleReviewScreen
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainScreen
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentSettingScreen
import com.hm.picplz.ui.screen.photographer_main.composable.PhotographerAddDeviceScreen
import com.hm.picplz.ui.screen.quick_shoot.QuickShootScreen
import com.hm.picplz.core.ui.R as CoreUiR

fun NavGraphBuilder.photographerNavGraph(navController: NavHostController) {
    composable<QuickShoot> {
        QuickShootScreen(mainNavController = navController)
    }

    composable<DetailPhotographer> {
        DetailPhotographerScreen(navController = navController)
    }

    composable<ReviewPhotographer> { backStackEntry ->
        val args = backStackEntry.toRoute<ReviewPhotographer>()
        DetailPhotographerReviewScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerPhotoReviews> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPhotoReviews>()
        DetailPhotographerPhotoReviewsScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerSingleReview> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerSingleReview>()
        // 리뷰 작성 직후 진입한 경우에만 등록 완료 토스트를 덮어서 띄웁니다.
        // 최초 컴포지션부터 visible이면 진입 애니메이션이 생략되므로 false로 시작해 다음 프레임에 켭니다.
        var showRegisteredToast by rememberSaveable { mutableStateOf(false) }
        var registeredToastShown by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (args.showRegisteredToast && !registeredToastShown) {
                registeredToastShown = true
                showRegisteredToast = true
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            DetailPhotographerSingleReviewScreen(
                navController = navController,
                reviewId = args.reviewId,
                photoIndex = args.photoIndex,
            )

            CommonToast(
                message = stringResource(CoreUiR.string.review_registered_toast),
                isVisible = showRegisteredToast,
                onDismiss = { showRegisteredToast = false },
            )
        }
    }

    composable<DetailPhotographerPhotoPortfolios> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPhotoPortfolios>()
        DetailPhotographerPhotoPortfoliosScreen(
            navController = navController,
            photographerId = args.photographerId,
        )
    }

    composable<DetailPhotographerPortfolioDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<DetailPhotographerPortfolioDetail>()
        DetailPhotographerPortfoliosScreen(
            navController = navController,
            portfolioId = args.portfolioId,
            photoIndex = args.photoIndex,
        )
    }

    navigation<PhotographerMainGraph>(startDestination = PhotographerMain) {
        composable<PhotographerMain> {
            val photographerMainViewModel = navController.sharedPhotographerMainViewModel()

            PhotographerMainScreen(
                viewModel = photographerMainViewModel,
                navController = navController,
            )
        }

        composable<PhotographerEquipmentSetting> { backStackEntry ->
            val args = backStackEntry.toRoute<PhotographerEquipmentSetting>()
            val photographerMainViewModel = navController.sharedPhotographerMainViewModel()

            EquipmentSettingScreen(
                viewModel = photographerMainViewModel,
                navController = navController,
                onNavigateBack = {
                    if (args.returnToMyPage) {
                        val poppedToMyPage = navController.popBackStack(MyPage, inclusive = false)
                        if (!poppedToMyPage) {
                            val poppedToPhotographerMyPage =
                                navController.popBackStack(
                                    route = MyPagePhotographer::class.qualifiedName.orEmpty(),
                                    inclusive = false,
                                )
                            if (!poppedToPhotographerMyPage) {
                                navController.popBackStack()
                            }
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
            )
        }

        composable<PhotographerAddDevice> { backStackEntry ->
            val args = backStackEntry.toRoute<PhotographerAddDevice>()
            val category =
                when (args.category.lowercase()) {
                    "camera" -> DeviceCategory.CAMERA
                    else -> DeviceCategory.PHONE
                }
            val photographerMainViewModel = navController.sharedPhotographerMainViewModel()

            PhotographerAddDeviceScreen(
                category = category,
                navController = navController,
                viewModel = photographerMainViewModel,
            )
        }
    }
}

@Composable
private fun NavHostController.sharedPhotographerMainViewModel(): PhotographerMainViewModel {
    val route = PhotographerMainGraph::class.qualifiedName.orEmpty()
    return hiltViewModel(getBackStackEntry(route))
}
