package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.ActiveAreaDto
import com.hm.picplz.data.model.CameraInfoDto
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotographerMapperTest {
    @Test
    fun `nearby photographer moods remove blank values hashes and duplicates`() {
        val photographer =
            NearbyPhotographerCard(
                photographerId = 7L,
                nickname = "유가영",
                profileImage = null,
                active = "Y",
                distance = 100L,
                photoMoods = listOf("캐주얼", null, " #캐주얼 ", "#자연광", " "),
            ).toDomain()

        assertEquals(listOf("캐주얼", "자연광"), photographer.photoMoods)
    }

    @Test
    fun `photographer detail maps runtime areas moods and cameras`() {
        val photographer =
            PhotographerDetailDto(
                photographerId = 7L,
                nickname = "유가영",
                profileImage = "profile.jpg",
                area = listOf(ActiveAreaDto(code = 1L, name = "마포구", priority = 1)),
                introduction = "",
                active = "Y",
                instagram = "imdooring",
                photoMoods = listOf("캐주얼", null, "#캐주얼", "#자연광"),
                cameras =
                    listOf(
                        CameraInfoDto(
                            type = "CAMERA",
                            brand = "Sony",
                            name = "A7IV",
                            cameraType = "미러리스",
                        ),
                    ),
                followers = 0,
                isFollowing = "N",
            ).toPhotographerInfo()

        assertEquals(listOf("마포구"), photographer.workingArea)
        assertEquals(listOf("캐주얼", "자연광"), photographer.keyword)
        assertEquals(listOf("Sony A7IV"), photographer.equipment)
    }
}
