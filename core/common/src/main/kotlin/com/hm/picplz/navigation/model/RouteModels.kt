package com.hm.picplz.navigation.model

import com.hm.picplz.common.model.User
import kotlinx.serialization.Serializable

sealed interface NavigationRoute

// === Graphs ===
@Serializable object AuthGraph : NavigationRoute

@Serializable object MainGraph : NavigationRoute

@Serializable object SignUpGraph : NavigationRoute

// === Auth Screens ===
@Serializable object Login : NavigationRoute

@Serializable
data class SignUpIntro(
    val profileImageUri: String? = null,
    val startAt: String? = null,
) : NavigationRoute

@Serializable
data class SignUpPhotographer(
    val userInfo: User? = null,
    val startAt: String? = null,
) : NavigationRoute

@Serializable
data class SignUpCompletion(val userInfo: User) : NavigationRoute

// === SignUp Common Internal Routes ===
@Serializable object SignUpSelectType : NavigationRoute

@Serializable object SignUpNickname : NavigationRoute

@Serializable object SignUpProfile : NavigationRoute

// === SignUp Photographer Internal Routes ===
@Serializable object SignUpMainLocation : NavigationRoute

@Serializable object SignUpExperience : NavigationRoute

@Serializable object SignUpDetailExperience : NavigationRoute

@Serializable object SignUpPhotographyVibe : NavigationRoute

@Serializable object SignUpCareerPeriod : NavigationRoute

@Serializable object SignUpDevice : NavigationRoute

@Serializable
data class SignUpAddDevice(val category: String = "phone") : NavigationRoute

// === Dev ===
@Serializable object Dev : NavigationRoute

@Serializable object DevMainSearchResultPreview : NavigationRoute

// === Main Screens ===
@Serializable object Main : NavigationRoute

@Serializable object MainSearch : NavigationRoute

@Serializable object Feed : NavigationRoute

@Serializable object Reservation : NavigationRoute

@Serializable object Chat : NavigationRoute

@Serializable object MyPage : NavigationRoute

@Serializable object MyPageFollowedPhotographers : NavigationRoute

@Serializable
data class MyPagePhotographer(
    val hasShootings: Boolean = false,
    val hasPackagePreview: Boolean = false,
    val hasPortfolioPreview: Boolean = false,
) : NavigationRoute

@Serializable
data class DevMyPagePhotographerProfileAdded(
    val hasShootings: Boolean = false,
    val hasPackagePreview: Boolean = false,
    val hasPortfolioPreview: Boolean = false,
) : NavigationRoute

@Serializable
data class DevMyPagePackageEdit(
    val packageCount: Int = 0,
) : NavigationRoute

@Serializable object MyPageModifyProfile : NavigationRoute

@Serializable object MyPagePhotographerModifyProfile : NavigationRoute

@Serializable
data class MyPagePhotographerKeywordEdit(
    val photographerId: Long,
) : NavigationRoute

@Serializable object MyPagePhotographerEquipmentSetting : NavigationRoute

@Serializable
data class MyPagePhotographerAddDevice(val category: String = "phone") : NavigationRoute

@Serializable
data class MyPagePackageEdit(val photographerId: Long) : NavigationRoute

@Serializable
data class MyPagePhotographerActiveAreaEdit(val photographerId: Int) : NavigationRoute

@Serializable object MyPageMyReviews : NavigationRoute

@Serializable
data class MyPageShootingHistory(
    val forceEmpty: Boolean = false,
) : NavigationRoute

@Serializable object MyPageOrderSheet : NavigationRoute

// === Photographer Screens ===
@Serializable object QuickShoot : NavigationRoute

@Serializable object PhotographerMainGraph : NavigationRoute

@Serializable object PhotographerMain : NavigationRoute

@Serializable
data class PhotographerEquipmentSetting(
    val returnToMyPage: Boolean = false,
) : NavigationRoute

@Serializable
data class PhotographerAddDevice(val category: String = "phone") : NavigationRoute

// === Detail Screens ===
@Serializable
data class DetailPhotographer(
    val photographerId: Int,
    val previewMode: Boolean = false,
) : NavigationRoute

@Serializable
data class ReviewPhotographer(val photographerId: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPhotoReviews(val photographerId: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPhotoPortfolios(val photographerId: Int) : NavigationRoute

// === Screens with Arguments ===
@Serializable
data class ChatRoom(val roomId: String) : NavigationRoute

@Serializable
data class DetailPhotographerSingleReview(val reviewId: Int, val photoIndex: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPortfolioDetail(val portfolioId: Int, val photoIndex: Int) : NavigationRoute

// === Reservation Screens ===
@Serializable
data object DetailReservation : NavigationRoute

@Serializable
data object PhotographerDetailReservation : NavigationRoute

@Serializable
data class PhotographerRejectReservation(val orderId: String) : NavigationRoute

@Serializable
data class CancelReservationConfirm(val isPhotographer: Boolean = false) : NavigationRoute

@Serializable
data class OrderDetail(val orderId: String) : NavigationRoute

@Serializable
data class CancelReservation(val orderId: String) : NavigationRoute

@Serializable
data class PhotographerCancelReservation(val orderId: String) : NavigationRoute

@Serializable
data class PhotographerChatRoom(val roomId: String) : NavigationRoute
