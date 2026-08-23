package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.ReservationDetailDto
import com.hm.picplz.domain.model.ReservationStatusCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

/**
 * dev 서버가 실제로 내려준 예약 상세 응답을 기준으로 매핑을 고정합니다.
 *
 * ```
 * {"reservationId":13,"packageName":"남친생기는 프사","productPrice":9900,"editPrice":0,
 *  "reservationTime":"2026-09-01T14:00:00","place":"서울특별시 종로구 효자로 33...",
 *  "photoAmount":5,"editedYn":"Y","reservationNumber":"N404e7a7c0964","status":"PENDING"}
 * ```
 */
class ReservationMapperTest {
    private fun dto(
        reservationTime: String? = "2026-09-01T14:00:00",
        editedYn: String? = "Y",
        status: String? = "PENDING",
    ) = ReservationDetailDto(
        reservationId = 13L,
        packageName = "남친생기는 프사",
        productPrice = 9900,
        editPrice = 0,
        reservationTime = reservationTime,
        place = "서울특별시 종로구 효자로 33",
        photoAmount = 5,
        editedYn = editedYn,
        reservationNumber = "N404e7a7c0964",
        status = status,
    )

    @Test
    fun `서버 응답이 도메인 모델로 변환된다`() {
        val detail = dto().toReservationDetail()

        assertEquals(13L, detail.reservationId)
        assertEquals("남친생기는 프사", detail.packageName)
        assertEquals(9900, detail.productPrice)
        assertEquals(5, detail.photoAmount)
        assertEquals("N404e7a7c0964", detail.reservationNumber)
        assertEquals(ReservationStatusCode.PENDING, detail.status)
    }

    @Test
    fun `촬영 일시가 로컬 타임존 기준 millis 로 파싱된다`() {
        val millis = requireNotNull(dto().toReservationDetail().shootingDateTimeMillis)

        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply { timeInMillis = millis }
        assertEquals(2026, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.SEPTEMBER, calendar.get(Calendar.MONTH))
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, calendar.get(Calendar.MINUTE))
    }

    @Test
    fun `초 단위 소수점이 붙어도 파싱된다`() {
        val withFraction = dto(reservationTime = "2026-09-01T14:00:00.123456").toReservationDetail()
        val plain = dto().toReservationDetail()

        assertEquals(plain.shootingDateTimeMillis, withFraction.shootingDateTimeMillis)
    }

    @Test
    fun `촬영 일시가 없거나 형식이 어긋나면 null 이다`() {
        assertNull(dto(reservationTime = null).toReservationDetail().shootingDateTimeMillis)
        assertNull(dto(reservationTime = "").toReservationDetail().shootingDateTimeMillis)
        assertNull(dto(reservationTime = "2026-09-01").toReservationDetail().shootingDateTimeMillis)
        assertNull(dto(reservationTime = "어제").toReservationDetail().shootingDateTimeMillis)
    }

    @Test
    fun `editedYn 이 Y 일 때만 보정 포함이다`() {
        assertEquals(true, dto(editedYn = "Y").toReservationDetail().isEdited)
        assertEquals(false, dto(editedYn = "N").toReservationDetail().isEdited)
        assertEquals(false, dto(editedYn = null).toReservationDetail().isEdited)
    }

    @Test
    fun `서버가 모르는 상태 문자열은 null 로 남는다`() {
        assertNull(dto(status = "COMPLETED").toReservationDetail().status)
        assertNull(dto(status = null).toReservationDetail().status)
    }

    @Test
    fun `누락된 숫자 필드는 0 으로 채운다`() {
        val detail =
            ReservationDetailDto(
                reservationId = 1L,
                packageName = null,
                productPrice = null,
                editPrice = null,
                reservationTime = null,
                place = null,
                photoAmount = null,
                editedYn = null,
                reservationNumber = null,
                status = null,
            ).toReservationDetail()

        assertEquals(0, detail.productPrice)
        assertEquals(0, detail.editPrice)
        assertEquals(0, detail.photoAmount)
        assertEquals("", detail.packageName)
        assertEquals("", detail.place)
        assertEquals("", detail.reservationNumber)
    }
}
