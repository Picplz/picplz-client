package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PhotographerPage

interface PhotographerSearchRepository {
    suspend fun searchPhotographers(
        keyword: String,
        sortType: String,
        page: Int,
        size: Int,
    ): AppResult<PhotographerPage>
}
