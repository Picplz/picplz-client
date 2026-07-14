package com.hm.picplz.ui.screen.main

internal fun devMainSearchPreviewState(): MainSearchState =
    MainSearchState.idle().copy(
        query = "유가영",
        hasSearched = true,
        results = devSearchPreviewPhotographers(),
        nearbyPhotographers = devSearchPreviewPhotographers(),
    )

internal fun devSearchPreviewPhotographers(): List<MainSearchPhotographerItem> =
    listOf(
        MainSearchPhotographerItem(
            id = "dev-search-active",
            name = "유가영 작가",
            profileImageUri = "https://picsum.photos/seed/picplz-search-1/240",
            areaSummary = "마포구, 서대문구",
            isAvailableNow = true,
            moodTags = listOf("을지로 감성", "MZ 감성", "필름 감성", "자연광", "빈티지"),
            distance = 100,
        ),
        MainSearchPhotographerItem(
            id = "dev-search-yeongdeungpo",
            name = "유가영 작가",
            profileImageUri = "https://picsum.photos/seed/picplz-search-2/240",
            areaSummary = "동작구, 영등포구",
            isAvailableNow = false,
            moodTags = listOf("을지로 감성", "MZ 감성", "MZ 감성"),
            distance = 200,
        ),
        MainSearchPhotographerItem(
            id = "dev-search-default",
            name = "유가영 작가",
            profileImageUri = null,
            areaSummary = "마포구, 망구",
            isAvailableNow = false,
            moodTags = listOf("을지로 감성", "MZ 감성", "MZ 감성"),
            distance = 300,
        ),
        MainSearchPhotographerItem(
            id = "dev-search-gangnam",
            name = "유가영 작가",
            profileImageUri = "https://picsum.photos/seed/picplz-search-4/240",
            areaSummary = "강남구, 강북구, 동대문구 외 3개",
            isAvailableNow = false,
            moodTags = listOf("을지로 감성", "MZ 감성", "MZ 감성"),
            distance = 400,
        ),
        MainSearchPhotographerItem(
            id = "dev-search-gangdong",
            name = "유가영 작가",
            profileImageUri = "https://picsum.photos/seed/picplz-search-5/240",
            areaSummary = "강동구",
            isAvailableNow = false,
            moodTags = listOf("을지로 감성", "MZ 감성", "MZ 감성"),
            distance = 500,
        ),
    )
