package com.dfinery.attribution.common.dto

import jakarta.validation.constraints.NotBlank

data class AdtouchDTO (
    val adKey: String?,
    @get: NotBlank(message = "AdtouchDTO.trackerId must not be blank")
    val trackerId: String,
    val createdAt: String?
)