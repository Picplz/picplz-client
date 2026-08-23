package com.hm.picplz.ui.screen.model

import com.hm.picplz.domain.model.ReservationStatusCode
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReservationStatusMapperTest {
    @Test
    fun `서버 상태가 화면 상태로 변환된다`() {
        assertEquals(ReservationStatus.WAITING_APPROVAL, ReservationStatusCode.PENDING.toUiStatusOrNull())
        assertEquals(ReservationStatus.WAITING_SCHEDULE, ReservationStatusCode.ACCEPTED.toUiStatusOrNull())
        assertEquals(ReservationStatus.RESERVED, ReservationStatusCode.CONFIRMED.toUiStatusOrNull())
    }

    @Test
    fun `거절 취소 상태는 대응하는 화면 상태가 없다`() {
        assertNull(ReservationStatusCode.REJECTED.toUiStatusOrNull())
        assertNull(ReservationStatusCode.CANCELED.toUiStatusOrNull())
    }

    @Test
    fun `서버가 모르는 문자열은 null 로 파싱된다`() {
        assertNull(ReservationStatusCode.from("COMPLETED"))
        assertNull(ReservationStatusCode.from(null))
        assertNull(ReservationStatusCode.from(""))
    }

    @Test
    fun `서버 상태 문자열이 그대로 파싱된다`() {
        ReservationStatusCode.entries.forEach { code ->
            assertEquals(code, ReservationStatusCode.from(code.name))
        }
    }
}
