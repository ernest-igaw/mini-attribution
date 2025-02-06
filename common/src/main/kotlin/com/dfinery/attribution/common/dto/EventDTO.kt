package com.dfinery.attribution.common.dto

import jakarta.validation.constraints.NotBlank

data class EventDTO (
    @get: NotBlank(message = "EventDTO.adId must not be blank")
    val adId: String,
    @get: NotBlank(message = "EventDTO.eventType must not be blank")
    val eventType: String,
    val logId: String?,
    @get: NotBlank(message = "EventDTO.adKey must not be blank")
    val adKey: String,
    val items: List<ItemDTO>? = null
)