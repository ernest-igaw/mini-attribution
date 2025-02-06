package com.dfinery.attribution.common.dto

import jakarta.validation.constraints.NotBlank

data class ProfileDTO (
    @get: NotBlank(message = "ProfileDTO.adId must not be blank")
    val adId: String,
    @get: NotBlank(message = "ProfileDTO.firstOpenLogId must not be blank")
    val firstOpenLogId: String,
    val adKey: String?,
    val trackerId: String?,
)