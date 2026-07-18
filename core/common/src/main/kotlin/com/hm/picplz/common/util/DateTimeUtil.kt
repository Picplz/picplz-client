package com.hm.picplz.common.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    private val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    private val deadlineFormat = SimpleDateFormat("yyyy.MM.dd | hh:mm까지", Locale.KOREA)
    private val dateTimeFormat = SimpleDateFormat("MM월 dd일 hh:mm", Locale.KOREA)
    private val reservationDateTimeFormat = SimpleDateFormat("yy.MM.dd a h:mm", Locale.KOREA)

    fun getFormattedTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun getFormattedDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun getFormattedDeadline(timestamp: Long): String {
        return deadlineFormat.format(Date(timestamp))
    }

    fun getFormattedDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    /**
     * 예약 확정 일시 표기용 포맷. (예: "25.12.12 오후 2:00")
     */
    fun getFormattedReservationDateTime(timestamp: Long): String {
        return reservationDateTimeFormat.format(Date(timestamp))
    }

    fun getTimeAgoText(timestamp: Long): String {
        val minutesAgo = ((System.currentTimeMillis() - timestamp) / (1000 * 60)).toInt()
        return when {
            minutesAgo < 1 -> "방금 전"
            minutesAgo < 60 -> "${minutesAgo}분 전"
            minutesAgo < 24 * 60 -> "${minutesAgo / 60}시간 전"
            else -> "${minutesAgo / (24 * 60)}일 전"
        }
    }

    fun truncateToDate(timestamp: Long): Long {
        val calendar =
            Calendar.getInstance().apply {
                timeInMillis = timestamp
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        return calendar.timeInMillis
    }

    // === 예약 관련 날짜 계산 함수 (API 24 안전) ===

    /**
     * 두 시간 사이의 시간 차이를 계산합니다. (버전 분기 처리)
     * @param startMillis 시작 시간 (milliseconds)
     * @param endMillis 종료 시간 (milliseconds)
     * @return 시간 차이
     */
    fun hoursBetween(
        startMillis: Long,
        endMillis: Long,
    ): Long {
        return (endMillis - startMillis) / (1000 * 60 * 60)
    }

    /**
     * 두 날짜 사이의 일수 차이를 계산합니다. (시간 제거 후 비교)
     * @param startMillis 시작 날짜 (milliseconds)
     * @param endMillis 종료 날짜 (milliseconds)
     * @return 일수 차이
     */
    fun daysBetween(
        startMillis: Long,
        endMillis: Long,
    ): Long {
        val startDate =
            Calendar.getInstance().apply {
                timeInMillis = startMillis
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        val endDate =
            Calendar.getInstance().apply {
                timeInMillis = endMillis
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        return (endDate.timeInMillis - startDate.timeInMillis) / (1000 * 60 * 60 * 24)
    }

    /**
     * 주어진 pattern 으로 날짜를 포맷팅합니다.
     * @param timestamp 날짜 (milliseconds)
     * @param pattern 포맷 패턴 (예: "yy.MM.dd")
     * @return 포맷된 문자열
     */
    fun formatDate(
        timestamp: Long,
        pattern: String,
    ): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(Date(timestamp))
    }

    /**
     * 주어진 날짜에서 days 만큼 더한 날짜를 반환합니다.
     * @param timestamp 시작 날짜 (milliseconds)
     * @param days 더할 일수
     * @return 더해진 날짜 (milliseconds)
     */
    fun plusDays(
        timestamp: Long,
        days: Int,
    ): Long {
        return Calendar.getInstance()
            .apply {
                timeInMillis = timestamp
                add(Calendar.DAY_OF_MONTH, days)
            }
            .timeInMillis
    }
}
