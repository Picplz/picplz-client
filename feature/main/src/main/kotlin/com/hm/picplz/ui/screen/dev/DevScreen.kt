package com.hm.picplz.ui.screen.dev

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.DetailPhotographerPhotoPortfolios
import com.hm.picplz.navigation.model.DetailPhotographerPhotoReviews
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.navigation.model.DevMainSearchResultPreview
import com.hm.picplz.navigation.model.DevMyPagePackageEdit
import com.hm.picplz.navigation.model.DevMyPagePhotographerProfileAdded
import com.hm.picplz.navigation.model.Feed
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.navigation.model.MyPagePackageEdit
import com.hm.picplz.navigation.model.MyPagePhotographer
import com.hm.picplz.navigation.model.MyPagePhotographerActiveAreaEdit
import com.hm.picplz.navigation.model.MyPagePhotographerModifyProfile
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.OrderDetail
import com.hm.picplz.navigation.model.PhotographerChatRoom
import com.hm.picplz.navigation.model.PhotographerDetailReservation
import com.hm.picplz.navigation.model.PhotographerEquipmentSetting
import com.hm.picplz.navigation.model.PhotographerMain
import com.hm.picplz.navigation.model.QuickShoot
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.navigation.model.ReviewPhotographer
import com.hm.picplz.navigation.model.SignUpCompletion
import com.hm.picplz.navigation.model.SignUpIntro
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.theme.MainThemeColor

/**
 * Dev 화면에서 예약 관련 화면으로 바로 진입할 때 쓰는 예약 id.
 *
 * dev 서버에 상태별 고정 예약을 만들어 뒀습니다 — **16=PENDING**, 17=ACCEPTED, 18=CONFIRMED.
 * 기본값은 승인/거절 버튼을 확인할 수 있는 PENDING 이고, 다른 상태를 보려면 값만 바꾸면 됩니다.
 * (예약 상세 조회는 작가 전용 API라 작가 토큰으로 로그인해야 데이터가 뜹니다.)
 */
private const val DEV_RESERVATION_ID = 16L

@Composable
fun DevScreen(
    navController: NavHostController,
    onLoginAsDevUser: () -> Unit,
    onLoginAsGuest: () -> Unit,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current
    val prepareDevPhotographerSignUp = {
        context.getSharedPreferences("picplz_auth", Context.MODE_PRIVATE)
            .edit()
            .putString("social_code", "dev-social-code")
            .putString("social_email", "dev-user@picplz.com")
            .putString("social_provider", "KAKAO")
            .apply()
    }
    val devPhotographerUserInfo =
        User(
            id = "dev-user",
            nickname = "테스트작가",
            email = "dev-photographer@picplz.com",
            userType = UserType.User,
            profileImageUri = null,
            profileImageObjectKey = null,
        )
    val devCustomerCompletionUserInfo =
        User(
            id = "dev-customer",
            nickname = "유가영",
            email = "dev-customer@picplz.com",
            userType = UserType.User,
            profileImageUri = null,
            profileImageObjectKey = null,
        )

    Scaffold(
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🛠 DEV MENU",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // === Token ===
            SectionTitle("Token")
            DevButton("개발 유저 로그인") {
                onLoginAsDevUser()
                Toast.makeText(context, "개발 유저 토큰 설정됨", Toast.LENGTH_SHORT).show()
            }
            DevButton("게스트 로그인") {
                onLoginAsGuest()
                Toast.makeText(context, "게스트 토큰 설정됨", Toast.LENGTH_SHORT).show()
            }
            DevButton("로그아웃") {
                onLogout()
                Toast.makeText(context, "토큰 삭제됨", Toast.LENGTH_SHORT).show()
            }
            DevButton("📋 현재 토큰 복사") {
                val prefs = context.getSharedPreferences("picplz_auth", Context.MODE_PRIVATE)
                val token = prefs.getString("access_token", null)
                if (token != null) {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("token", token))
                    Toast.makeText(context, "토큰 복사됨 (${token.take(20)}...)", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "저장된 토큰 없음", Toast.LENGTH_SHORT).show()
                }
            }

            // === Auth ===
            SectionTitle("Auth")
            DevButton("Login") { navController.navigate(Login) }
            DevButton("SignUpIntro") { navController.navigate(SignUpIntro()) }
            DevButton("SignUpSelectType (유형 선택)") {
                navController.navigate(SignUpIntro(startAt = "select-type"))
            }
            DevButton("SignUpNickname (닉네임)") {
                navController.navigate(SignUpIntro(startAt = "nickname"))
            }
            DevButton("SignUpProfile (프로필 이미지)") {
                navController.navigate(SignUpIntro(startAt = "profile"))
            }
            DevButton("SignUpPhotographer") {
                prepareDevPhotographerSignUp()
                navController.navigate(SignUpPhotographer(userInfo = devPhotographerUserInfo))
            }
            DevButton("SignUpDevice (Direct)") {
                prepareDevPhotographerSignUp()
                navController.navigate(
                    SignUpPhotographer(
                        userInfo = devPhotographerUserInfo,
                        startAt = "device",
                    ),
                )
            }
            DevButton("SignUpPhotographyVibe (분위기 키워드)") {
                prepareDevPhotographerSignUp()
                navController.navigate(
                    SignUpPhotographer(
                        userInfo = devPhotographerUserInfo,
                        startAt = "vibe",
                    ),
                )
            }
            DevButton("SignUpCompletion (고객/기본 이미지)") {
                navController.navigate(SignUpCompletion(userInfo = devCustomerCompletionUserInfo))
            }

            // === Main Tabs ===
            SectionTitle("Main Tabs")
            DevButton("Main (홈)") { navController.navigate(Main) }
            DevButton("MainSearch (검색)") { navController.navigate(MainSearch) }
            DevButton("MainSearch (검색완료 카드 목업)") { navController.navigate(DevMainSearchResultPreview) }
            DevButton("Feed (피드)") { navController.navigate(Feed) }
            DevButton("Reservation (예약)") { navController.navigate(Reservation) }
            DevButton("Chat (채팅)") { navController.navigate(Chat) }
            DevButton("MyPage (마이페이지)") { navController.navigate(MyPage) }
            DevButton("MyPage DEV (작가 프로필 추가됨 - 기본)") {
                navController.navigate(DevMyPagePhotographerProfileAdded())
            }
            DevButton("MyPage DEV (작가 프로필 추가됨 - 전체)") {
                navController.navigate(
                    DevMyPagePhotographerProfileAdded(
                        hasShootings = true,
                        hasPackagePreview = true,
                        hasPortfolioPreview = true,
                    ),
                )
            }
            DevButton("MyPage (작가 미리보기 테스트)") {
                navController.navigate(
                    MyPagePhotographer(
                        hasPackagePreview = true,
                        hasPortfolioPreview = false,
                    ),
                )
            }

            // === Main Sub Screens ===
            SectionTitle("Main Sub")
            DevButton("MainSearch") { navController.navigate(MainSearch) }
            DevButton("MyPageModifyProfile") { navController.navigate(MyPageModifyProfile) }
            DevButton("MyPagePhotographerModifyProfile") { navController.navigate(MyPagePhotographerModifyProfile) }
            DevButton("MyPagePackageEdit DEV (패키지 0개)") {
                navController.navigate(DevMyPagePackageEdit(packageCount = 0))
            }
            DevButton("MyPagePackageEdit DEV (패키지 1개)") {
                navController.navigate(DevMyPagePackageEdit(packageCount = 1))
            }
            DevButton("MyPagePackageEdit DEV (패키지 2개)") {
                navController.navigate(DevMyPagePackageEdit(packageCount = 2))
            }
            DevButton("MyPagePackageEdit DEV (패키지 3개)") {
                navController.navigate(DevMyPagePackageEdit(packageCount = 3))
            }
            DevButton("MyPagePackageEdit (실제 route)") {
                navController.navigate(MyPagePackageEdit(photographerId = 1L))
            }
            DevButton("MyPagePhotographerActiveAreaEdit") {
                navController.navigate(MyPagePhotographerActiveAreaEdit(photographerId = 7))
            }
            DevButton("MyPagePhotographerModifyProfile (권한 설정 → 거부 후 테스트)") {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${context.packageName}"),
                    ),
                )
            }
            DevButton("MyPageShootingHistory") { navController.navigate(MyPageShootingHistory()) }
            DevButton("MyPageShootingHistory (Empty)") {
                navController.navigate(MyPageShootingHistory(forceEmpty = true))
            }
            DevButton("MyPageOrderSheet") { navController.navigate(MyPageOrderSheet) }
            // === Photographer ===
            SectionTitle("Photographer")
            DevButton("QuickShoot (빠른촬영)") { navController.navigate(QuickShoot) }
            DevButton("QuickShoot (권한 설정 → 거부 후 테스트)") {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${context.packageName}"),
                    ),
                )
            }
            DevButton("PhotographerMain (작가홈)") { navController.navigate(PhotographerMain) }
            DevButton("PhotographerEquipmentSetting") {
                navController.navigate(
                    PhotographerEquipmentSetting(),
                )
            }

            // === Detail Photographer ===
            SectionTitle("Detail Photographer")
            DevButton("DetailPhotographer (1, error test)") { navController.navigate(DetailPhotographer(1)) }
            DevButton("DetailPhotographer (14, sparse DB)") { navController.navigate(DetailPhotographer(14)) }
            DevButton("DetailPhotographer (7, rich DB)") { navController.navigate(DetailPhotographer(7)) }
            DevButton("DetailPhotographer (작가 preview mode)") {
                navController.navigate(DetailPhotographer(1, previewMode = true))
            }
            DevButton("DetailPhotographer (차단)") { navController.navigate(DetailPhotographer(-1)) }
            DevButton("ReviewPhotographer") { navController.navigate(ReviewPhotographer(1)) }
            DevButton("DetailPhotographerPhotoReviews") {
                navController.navigate(
                    DetailPhotographerPhotoReviews(1),
                )
            }
            DevButton("DetailPhotographerPhotoPortfolios") {
                navController.navigate(
                    DetailPhotographerPhotoPortfolios(1),
                )
            }

            // === Chat ===
            SectionTitle("Chat")
            DevButton("ChatRoom (test-room)") { navController.navigate(ChatRoom(roomId = "test-room-123")) }
            DevButton("PhotographerChatRoom (작가 입장)") {
                navController.navigate(PhotographerChatRoom(roomId = "test-room-123"))
            }

            // === Reservation ===
            SectionTitle("Reservation")
            DevButton(
                "DetailReservation(예약 상세)",
            ) { navController.navigate(DetailReservation(reservationId = DEV_RESERVATION_ID)) }
            DevButton(
                "OrderDetail(결제 후 취소 주문 상세)",
            ) { navController.navigate(OrderDetail(reservationId = DEV_RESERVATION_ID)) }
            DevButton(
                "PhotographerDetailReservation(작가 예약 상세)",
            ) { navController.navigate(PhotographerDetailReservation(reservationId = DEV_RESERVATION_ID)) }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MainThemeColor.Gray3)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MainThemeColor.Gray5,
        )
    }
}

@Composable
private fun DevButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Black,
                contentColor = Color.White,
            ),
    ) {
        Text(text = label)
    }
}
