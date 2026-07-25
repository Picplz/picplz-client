package com.hm.picplz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hm.picplz.common.model.UserType
import com.hm.picplz.navigation.graph.authNavGraph
import com.hm.picplz.navigation.graph.mainNavGraph
import com.hm.picplz.navigation.graph.photographerNavGraph
import com.hm.picplz.navigation.model.CancelReservation
import com.hm.picplz.navigation.model.CancelReservationConfirm
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.Main
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
import com.hm.picplz.navigation.model.PhotographerMainGraph
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.ui.main.MainActivityUiState
import kotlin.reflect.KClass

private val authRequiredRoutes: List<KClass<out Any>> =
    listOf(
        Reservation::class,
        Chat::class,
        ChatRoom::class,
        PhotographerChatRoom::class,
        MyPage::class,
        MyPageFollowedPhotographers::class,
        MyPagePhotographer::class,
        MyPageModifyProfile::class,
        MyPagePhotographerModifyProfile::class,
        MyPagePhotographerKeywordEdit::class,
        MyPagePhotographerEquipmentSetting::class,
        MyPagePhotographerAddDevice::class,
        MyPagePackageEdit::class,
        MyPagePhotographerActiveAreaEdit::class,
        MyPageMyReviews::class,
        MyPageShootingHistory::class,
        MyPageOrderSheet::class,
        DetailReservation::class,
        PhotographerDetailReservation::class,
        CancelReservationConfirm::class,
        OrderDetail::class,
        CancelReservation::class,
        PhotographerCancelReservation::class,
    )

@Composable
fun MainNavHost(
    navController: NavHostController,
    uiState: MainActivityUiState,
    refreshUserData: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (uiState is MainActivityUiState.Loading) return

    val startDestination: Any = startDestinationFor(uiState)
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    LaunchedEffect(uiState, currentDestination) {
        when {
            uiState == MainActivityUiState.Unauthenticated && currentDestination.requiresAuth() -> {
                navController.navigate(Login) {
                    launchSingleTop = true
                }
            }
            uiState is MainActivityUiState.Success && currentDestination?.hasRoute(Login::class) == true -> {
                navController.navigate(startDestinationFor(uiState)) {
                    popUpTo(Login) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(
            navController = navController,
            onLoginCompleted = refreshUserData,
            onSignupCompleted = refreshUserData,
        )
        mainNavGraph(navController)
        photographerNavGraph(navController)
    }
}

fun startDestinationFor(uiState: MainActivityUiState): Any =
    when (uiState) {
        is MainActivityUiState.Success ->
            if (uiState.userData.userType == UserType.Photographer) PhotographerMainGraph else Main
        MainActivityUiState.Unauthenticated -> Login
        MainActivityUiState.Loading -> Main
    }

private fun NavDestination?.requiresAuth(): Boolean {
    if (this == null) return false

    return authRequiredRoutes.any { hasRoute(it) }
}
