package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PhotographerPage
import com.hm.picplz.domain.repository.PhotographerSearchRepository
import javax.inject.Inject

class SearchPhotographersUseCase
    @Inject
    constructor(
        private val photographerSearchRepository: PhotographerSearchRepository,
    ) {
        suspend operator fun invoke(
            keyword: String,
            sortType: String,
            page: Int,
            size: Int,
        ): AppResult<PhotographerPage> =
            photographerSearchRepository.searchPhotographers(
                keyword = keyword,
                sortType = sortType,
                page = page,
                size = size,
            )
    }
