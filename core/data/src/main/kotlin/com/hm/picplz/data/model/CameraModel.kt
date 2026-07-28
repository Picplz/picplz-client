package com.hm.picplz.data.model

import com.google.gson.annotations.SerializedName

data class CameraInfoDto(
    @SerializedName("type") val type: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("name") val name: String,
    @SerializedName("cameraType") val cameraType: String? = null,
)

data class CameraListData(
    val brands: List<DeviceBrand>,
    val types: List<String>,
)
