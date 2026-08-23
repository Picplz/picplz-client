package com.hm.picplz.ui.screen.model

import com.hm.picplz.ui.screen.cancel_reservation.CancelReason
import com.hm.picplz.ui.screen.photographer_cancel_reservation.PhotographerCancelReason
import com.hm.picplz.ui.screen.photographer_reject_reservation.PhotographerRejectReason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 서버가 받는 사유 enum 이름을 고정합니다.
 *
 * 문자열이 하나라도 틀리면 서버가 400을 돌려주는데 화면상으로는 "취소 실패"로만 보여서
 * 원인을 찾기 어렵습니다. 그래서 매핑을 값 그대로 검증합니다.
 */
class ReservationReasonMapperTest {
    /** dev 서버 `CancelReservationRequest.cancelReasons` 가 허용하는 11개 값 */
    private val serverCancelReasons =
        setOf(
            "SCHEDULE_CONFLICT",
            "PRODUCT_CHANGE_REQUEST",
            "LOCATION_ISSUE",
            "CHANGE_OF_MIND",
            "PHOTOGRAPHER_NO_RESPONSE",
            "ADDITIONAL_PAYMENT_REQUEST",
            "HEALTH_ISSUE",
            "CUSTOMER_NO_RESPONSE",
            "ACCIDENTAL_ACCEPTANCE",
            "EQUIPMENT_FAILURE",
            "EXTERNAL_CIRCUMSTANCE",
        )

    /** dev 서버 `RejectReservationRequest.rejectReasons` 가 허용하는 3개 값 */
    private val serverRejectReasons =
        setOf("REGION_UNAVAILABLE", "TIME_UNAVAILABLE", "EQUIPMENT_ISSUE")

    @Test
    fun `고객 취소 사유가 서버 enum 이름으로 변환된다`() {
        assertEquals("SCHEDULE_CONFLICT", CancelReason.SCHEDULE.toServerReason())
        assertEquals("PRODUCT_CHANGE_REQUEST", CancelReason.PRODUCT.toServerReason())
        assertEquals("LOCATION_ISSUE", CancelReason.LOCATION.toServerReason())
        assertEquals("CHANGE_OF_MIND", CancelReason.MIND.toServerReason())
        assertEquals("PHOTOGRAPHER_NO_RESPONSE", CancelReason.PHOTOGRAPHER_RESPONSE.toServerReason())
        assertEquals("ADDITIONAL_PAYMENT_REQUEST", CancelReason.PHOTOGRAPHER_EXTRA_PAYMENT.toServerReason())
    }

    @Test
    fun `작가 취소 사유가 서버 enum 이름으로 변환된다`() {
        assertEquals("SCHEDULE_CONFLICT", PhotographerCancelReason.SCHEDULE.toServerReason())
        assertEquals("HEALTH_ISSUE", PhotographerCancelReason.HEALTH.toServerReason())
        assertEquals("CUSTOMER_NO_RESPONSE", PhotographerCancelReason.CUSTOMER_RESPONSE.toServerReason())
        assertEquals("ACCIDENTAL_ACCEPTANCE", PhotographerCancelReason.MISTAKE.toServerReason())
        assertEquals("EQUIPMENT_FAILURE", PhotographerCancelReason.EQUIPMENT.toServerReason())
        assertEquals("EXTERNAL_CIRCUMSTANCE", PhotographerCancelReason.EXTERNAL.toServerReason())
    }

    @Test
    fun `거절 사유가 서버 enum 이름으로 변환된다`() {
        assertEquals("REGION_UNAVAILABLE", PhotographerRejectReason.AREA.toServerReason())
        assertEquals("TIME_UNAVAILABLE", PhotographerRejectReason.TIME.toServerReason())
        assertEquals("EQUIPMENT_ISSUE", PhotographerRejectReason.EQUIPMENT.toServerReason())
    }

    @Test
    fun `직접 입력은 서버 enum 이 없어 null 이다`() {
        assertNull(CancelReason.DIRECT_INPUT.toServerReason())
        assertNull(PhotographerCancelReason.DIRECT_INPUT.toServerReason())
        assertNull(PhotographerRejectReason.DIRECT_INPUT.toServerReason())
    }

    @Test
    fun `변환 결과가 모두 서버가 아는 값이다`() {
        CancelReason.entries.mapNotNull { it.toServerReason() }.forEach {
            assertTrue("서버가 모르는 취소 사유: $it", it in serverCancelReasons)
        }
        PhotographerCancelReason.entries.mapNotNull { it.toServerReason() }.forEach {
            assertTrue("서버가 모르는 취소 사유: $it", it in serverCancelReasons)
        }
        PhotographerRejectReason.entries.mapNotNull { it.toServerReason() }.forEach {
            assertTrue("서버가 모르는 거절 사유: $it", it in serverRejectReasons)
        }
    }

    @Test
    fun `서로 다른 선택지가 같은 서버 값으로 겹치지 않는다`() {
        val customer = CancelReason.entries.mapNotNull { it.toServerReason() }
        val photographer = PhotographerCancelReason.entries.mapNotNull { it.toServerReason() }
        assertEquals(customer.size, customer.toSet().size)
        assertEquals(photographer.size, photographer.toSet().size)
        // 고객 6종 + 작가 6종에서 SCHEDULE_CONFLICT 만 공유하므로 합집합은 11종이 된다.
        assertEquals(serverCancelReasons, (customer + photographer).toSet())
    }

    @Test
    fun `선택 집합은 화면 노출 순서를 유지하고 직접 입력은 제외한다`() {
        val selected = setOf(CancelReason.DIRECT_INPUT, CancelReason.MIND, CancelReason.SCHEDULE)

        assertEquals(listOf("SCHEDULE_CONFLICT", "CHANGE_OF_MIND"), selected.toServerReasons())
    }

    @Test
    fun `직접 입력만 고른 경우 서버 사유 목록은 비어 있다`() {
        assertEquals(emptyList<String>(), setOf(CancelReason.DIRECT_INPUT).toServerReasons())
        assertEquals(
            emptyList<String>(),
            setOf(PhotographerCancelReason.DIRECT_INPUT).toServerReasons(),
        )
    }
}
