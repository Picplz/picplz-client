package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.PhotographerPage
import javax.inject.Inject

interface PhotographerSearchService {
    suspend fun searchPhotographers(
        keyword: String,
        sortType: String,
        page: Int,
        size: Int,
    ): AppResult<PhotographerPage>
}

class PhotographerSearchServiceImpl
    @Inject
    constructor(
        private val photographerSource: PhotographerSource,
    ) : PhotographerSearchService {
        override suspend fun searchPhotographers(
            keyword: String,
            sortType: String,
            page: Int,
            size: Int,
        ): AppResult<PhotographerPage> =
            photographerSource.searchPhotographers(keyword, sortType, page, size).map { it.toDomain() }
    }
